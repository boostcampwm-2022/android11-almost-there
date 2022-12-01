package com.woory.presentation.background.service

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.woory.data.repository.PromiseRepository
import com.woory.data.repository.RouteRepository
import com.woory.presentation.R
import com.woory.presentation.background.notification.NotificationChannelProvider
import com.woory.presentation.background.notification.NotificationProvider
import com.woory.presentation.background.util.asPromiseAlarm
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.model.mapper.location.asDomain
import com.woory.presentation.model.mapper.promise.asUiModel
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PromiseReadyService : LifecycleService() {
    @Inject
    lateinit var routeRepository: RouteRepository

    @Inject
    lateinit var promiseRepository: PromiseRepository
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
                val dest = promise.data.promiseLocation.geoPoint

                getLastLocation() { start ->
                    lifecycleScope.launch {
                        withContext(this.coroutineContext) {
                            routeRepository.getMaximumVelocity(start.asDomain(), dest.asDomain())
                                .onSuccess {
                                    Log.d("TAG", "성공 -> $it")
                                }
                                .onFailure {
                                    Log.d("TAG", "실패 -> $it")
                                }
                                .also {
                                    stopSelf()
                                }
                        }
                    }
                }
            }

            notifyReadyCompleteNotification(promiseAlarm)
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

    private fun getLastLocation(callback: (GeoPoint) -> Unit) {
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
            it ?: return@addOnSuccessListener
            val geoPoint = GeoPoint(it.latitude, it.longitude)
            Log.d("TAG", "현재 -> $geoPoint")
            callback(geoPoint)
        }
    }
}