package com.woory.almostthere.presentation.background.service

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.woory.almostthere.data.repository.PromiseRepository
import com.woory.almostthere.data.repository.RouteRepository
import com.woory.almostthere.data.repository.UserRepository
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.background.notification.NotificationChannelProvider
import com.woory.almostthere.presentation.background.notification.NotificationProvider
import com.woory.almostthere.presentation.background.util.asPromiseAlarm
import com.woory.almostthere.presentation.model.GeoPoint
import com.woory.almostthere.presentation.model.PromiseAlarm
import com.woory.almostthere.presentation.model.UserLocation
import com.woory.almostthere.presentation.model.mapper.location.asDomain
import com.woory.almostthere.presentation.model.mapper.promise.asUiModel
import com.woory.almostthere.presentation.ui.promiseinfo.PromiseInfoActivity
import com.woory.almostthere.presentation.util.TimeConverter.asMillis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PromiseReadyService : LifecycleService() {
    @Inject
    lateinit var routeRepository: RouteRepository

    @Inject
    lateinit var promiseRepository: PromiseRepository

    @Inject
    lateinit var userRepository: UserRepository

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate() {
        super.onCreate()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationChannelProvider.provideServiceChannel(this)
        }

        val notification = NotificationProvider.createNotificationBuilder(
            this,
            NotificationChannelProvider.PROMISE_READY_SERVICE_CHANNEL_ID,
            getString(R.string.notification_ready_progress_title),
            null,
            NotificationCompat.PRIORITY_LOW,
            null
        ).build()

        startForeground(83, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        intent?.let {
            val promiseAlarm = it.asPromiseAlarm()

            lifecycleScope.launch {
                val promise = promiseRepository.getPromiseByCode(promiseAlarm.promiseCode)
                    .getOrThrow()
                    .asUiModel()

                val gameDuration =
                    Duration.between(promise.data.gameDateTime, promise.data.promiseDateTime)
                        .toMinutes()
                val destGeoPoint = promise.data.promiseLocation.geoPoint

                getLastLocation(promiseAlarm) { startGeoPoint ->
                    lifecycleScope.launch {

                        launch {
                            userRepository.userPreferences.collectLatest {
                                val userToken = it.userID
                                val currentUserLocation = UserLocation(
                                    userToken,
                                    startGeoPoint,
                                    OffsetDateTime.now().asMillis()
                                )
                                promiseRepository.setUserLocation(currentUserLocation.asDomain())
                            }
                        }

                        withContext(this.coroutineContext) {
                            routeRepository.getMaximumVelocity(
                                startGeoPoint.asDomain(),
                                destGeoPoint.asDomain()
                            )
                                .onSuccess { velocity ->
                                    val maxRadius = velocity * gameDuration
                                    promiseRepository.updateMagneticRadius(promise.code, maxRadius)
                                }
                                .onFailure { error ->
                                    Timber.tag("TAG").d("Error -> $error")
                                }
                                .also {
                                    stopService(promiseAlarm)
                                }
                        }
                    }
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun notifyReadyCompleteNotification(promiseAlarm: PromiseAlarm) {
        NotificationProvider.notifyActivityNotification(
            this,
            this.getString(R.string.notification_ready_complete_title),
            this.getString(R.string.notification_ready_complete_content),
            promiseAlarm,
            PromiseInfoActivity::class.java
        )
    }

    private fun getLastLocation(promiseAlarm: PromiseAlarm, callback: (GeoPoint) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            it ?: stopService(promiseAlarm)

            val geoPoint = GeoPoint(it.latitude, it.longitude)
            callback(geoPoint)
        }
    }

    private fun stopService(promiseAlarm: PromiseAlarm) {
        stopSelf()
        notifyReadyCompleteNotification(promiseAlarm)
    }
}