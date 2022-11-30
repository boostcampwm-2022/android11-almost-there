package com.woory.presentation.background.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.woory.data.repository.PromiseRepository
import com.woory.data.repository.UserRepository
import com.woory.presentation.R
import com.woory.presentation.background.notification.NotificationChannelProvider
import com.woory.presentation.background.notification.NotificationProvider
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.MagneticInfo
import com.woory.presentation.model.UserLocation
import com.woory.presentation.model.mapper.location.asDomain
import com.woory.presentation.ui.gaming.GamingActivity
import com.woory.presentation.util.TimeConverter.asMillis
import com.woory.presentation.util.TimeConverter.asOffsetDateTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PromiseGameService : LifecycleService() {

    @Inject
    lateinit var repository: PromiseRepository

    @Inject
    lateinit var userRepository: UserRepository

    private val _userId: MutableStateFlow<String?> = MutableStateFlow(null)
    private val userId: StateFlow<String?> = _userId.asStateFlow()

    private val _magneticInfo: MutableStateFlow<MagneticInfo?> = MutableStateFlow(null)
    private val magneticInfo: StateFlow<MagneticInfo?> = _magneticInfo.asStateFlow()

    private val _hp: MutableStateFlow<Int> = MutableStateFlow(defaultHpValue)
    val hp: StateFlow<Int> = _hp.asStateFlow()

    private val _location: MutableStateFlow<GeoPoint?> = MutableStateFlow(null)
    val location: StateFlow<GeoPoint?> = _location.asStateFlow()

    private val client: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val callback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            val loc = p0.lastLocation
            loc?.let {
                lifecycleScope.launch {
                    userId.value?.let { id ->
                        repository.setUserLocation(
                            UserLocation(id, GeoPoint(it.latitude, it.longitude)).asDomain()
                        )
                    }
//                    _location.emit(GeoPoint(it.latitude, it.longitude))
                }
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        startForeground()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userRepository.userPreferences.collect {
                    _userId.emit(it.userID)
                }
            }
        }
        client.requestLocationUpdates(
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000 * 20).build(),
            callback,
            Looper.getMainLooper()
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val gameCode = intent?.getStringExtra(GAME_CODE_KEY) ?: return@repeatOnLifecycle

                repository.updateMagneticRadius(gameCode, 20.0)

                repository.getPromiseByCode(gameCode).onSuccess {
                    Timber.tag("123123").d("Promies Success")
                    val curTime = System.currentTimeMillis().asOffsetDateTime()
                    repository.getMagneticInfoByCode(gameCode).onSuccess { magneticInfo ->
                        when {
                            // 첫 업데이트 일 때
                            magneticInfo.updatedAt < curTime.minusSeconds(30) -> {
                                Timber.tag("123123").d("first")
                                while (true) {
                                    Timber.tag("123123").d("updated first")
                                    delay(1000 * 30)
                                    repository.decreaseMagneticRadius(gameCode)
                                }
                            }

                            // 이후 업데이트 일 때
                            else -> {
                                Timber.tag("123123").d("second")
                                val timeToSleep =
                                    magneticInfo.updatedAt.plusMinutes(1)
                                        .asMillis() - System.currentTimeMillis()
                                delay(timeToSleep)
                                while (true) {
                                    Timber.tag("123123").d("updated second")
                                    delay(1000 * 30)
                                    repository.decreaseMagneticRadius(gameCode)
                                }
                            }
                        }

                        // TODO : HP 업데이트

                    }.onFailure {
                        makeToast("자기장 로딩에 실패했습니다.")
                        return@repeatOnLifecycle
                    }
                }.onFailure {
                    Timber.tag("123123").d("Promies Fail")
                    makeToast("게임 로딩에 실패했습니다.")
                    return@repeatOnLifecycle
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeground() {
        val intent = Intent(this, GamingActivity::class.java)

        val pendingIntent: PendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                NotificationProvider.PROMISE_START_NOTIFICATION_ID,
                PendingIntent.FLAG_IMMUTABLE
            )
        } ?: return

        val notification = NotificationProvider.createNotificationBuilder(
            this,
            NotificationChannelProvider.PROMISE_START_CHANNEL_ID,
            getString(R.string.notification_start_title),
            getString(R.string.notification_start_content),
            NotificationCompat.PRIORITY_HIGH,
            pendingIntent,
        ).build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationChannelProvider.providePromiseStartChannel(this)
        }
        startForeground(NotificationProvider.PROMISE_START_NOTIFICATION_ID, notification)
    }

    private fun makeToast(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val GAME_CODE_KEY = "code"
        private const val defaultHpValue = 100
    }
}
