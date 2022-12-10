package com.woory.presentation.background.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Build
import android.os.Looper
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
import com.woory.presentation.background.util.asPromiseAlarm
import com.woory.presentation.background.util.putPromiseAlarm
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.MagneticInfo
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.model.UserLocation
import com.woory.presentation.model.mapper.location.asDomain
import com.woory.presentation.model.mapper.magnetic.asUiModel
import com.woory.presentation.model.mapper.promise.asUiModel
import com.woory.presentation.ui.gaming.GamingActivity
import com.woory.presentation.util.DistanceUtil
import com.woory.presentation.util.TimeConverter.asMillis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

@AndroidEntryPoint
class PromiseGameService : LifecycleService() {

    @Inject
    lateinit var promiseRepository: PromiseRepository

    @Inject
    lateinit var userRepository: UserRepository

    private val jobByGame = mutableMapOf<String, Job>()

    private val _userId: MutableStateFlow<String?> = MutableStateFlow(null)
    private val userId: StateFlow<String?> = _userId.asStateFlow()

    private val _magneticZoneInitialRadius: MutableStateFlow<Double> = MutableStateFlow(
        INITIAL_MAGNETIC_FIELD_RADIUS)
    private val magneticZoneInitialRadius: StateFlow<Double> =
        _magneticZoneInitialRadius.asStateFlow()

    private val _gameTimeInitialValue: MutableStateFlow<Int> = MutableStateFlow(INITIAL_GAME_TIME)
    private val gameTimeInitialValue: StateFlow<Int> = _gameTimeInitialValue.asStateFlow()

    private val _magneticZoneInfo: MutableStateFlow<MagneticInfo?> = MutableStateFlow(null)
    private val magneticZoneInfo: StateFlow<MagneticInfo?> = _magneticZoneInfo.asStateFlow()

    private val _location: MutableStateFlow<GeoPoint?> = MutableStateFlow(null)
    val location: StateFlow<GeoPoint?> = _location.asStateFlow()

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            val loc = p0.lastLocation
            loc?.let { location ->
                lifecycleScope.launch {
                    val curLocation = GeoPoint(location.latitude, location.longitude)
                    userId.value?.let { id ->
                        promiseRepository.setUserLocation(
                            UserLocation(id, curLocation, System.currentTimeMillis()).asDomain()
                        )

                        magneticZoneInfo.value?.let {
                            if (DistanceUtil.getDistance(it.centerPoint, curLocation) > it.radius) {
                                launch {
                                    promiseRepository.decreaseUserHp(it.gameCode, id)
                                        .onSuccess { updatedHp ->
                                            if (updatedHp <= 0L) {
                                                stopUpdateLocation()
                                            }
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                userRepository.userPreferences.collectLatest {
                    _userId.emit(it.userID)
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                1000 * LOCATION_UPDATE_SECOND_INTERVAL
            ).build(),
            locationCallback,
            Looper.getMainLooper()
        )
    }

    @Throws(IllegalArgumentException::class)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent ?: throw IllegalArgumentException("has not intent")
        startForeground(intent.asPromiseAlarm())

        val promiseAlarm = intent.asPromiseAlarm()
        val promiseCode = promiseAlarm.promiseCode

        val gameJob = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val userToken = userId.value ?: return@repeatOnLifecycle

                promiseRepository.checkReEntryOfGame(promiseCode, userToken).onSuccess {
                    when (it) {
                        true -> {
                            promiseRepository.setUserInitialHpData(promiseCode, userToken)
                                .onSuccess {
                                    promiseRepository.getPromiseByCode(promiseCode)
                                        .onSuccess { promiseModel ->
                                            val promiseUiModel = promiseModel.asUiModel()

                                            if (promiseUiModel.data.gameDateTime.asMillis() - System.currentTimeMillis() > 20 * 1000) {
                                                promiseRepository.sendOutUser(
                                                    promiseCode,
                                                    userToken
                                                )
                                                stopUpdateLocation()
                                            } else {
                                                _gameTimeInitialValue.emit(
                                                    extractTimeDifference(
                                                        promiseUiModel.data.gameDateTime,
                                                        promiseUiModel.data.promiseDateTime
                                                    )
                                                )

                                                // TODO : 자기장 flow 받아오기
                                                launch {
                                                    promiseRepository.getMagneticInfoByCodeAndListen(
                                                        promiseCode
                                                    )
                                                        .collect { result ->
                                                            result.onSuccess { magneticInfoModel ->
                                                                val uiModel =
                                                                    magneticInfoModel.asUiModel()
                                                                _magneticZoneInfo.emit(uiModel)
                                                            }
                                                        }
                                                }

                                                launch {
                                                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                                                        promiseRepository.getIsFinishedPromise(
                                                            promiseCode
                                                        ).collectLatest { result ->
                                                            result.onSuccess { isFinished ->
                                                                if (isFinished) {
                                                                    stopGame(promiseCode)
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                // TODO : 주기적으로 자기장 update 하기
                                                while (true) {
                                                    delay((1000 * MAGNETIC_FIELD_UPDATE_SECOND_INTERVAL).toLong())
                                                    promiseRepository.decreaseMagneticRadius(
                                                        promiseCode,
                                                        magneticZoneInitialRadius.value / gameTimeInitialValue.value
                                                    )

                                                }
                                            }
                                        }
                                }
                        }
                        // TODO : Service 에 다시 즐어왔을 때 로직 -> 게임에서 제외시켜버리기
                        false -> {
                            promiseRepository.sendOutUser(promiseCode, userToken)
                            stopUpdateLocation()
                        }
                    }
                }.onFailure {
                    // TODO : 통신 실패시 로직 -> 게임에서 제외시켜버리기
                    promiseRepository.sendOutUser(promiseCode, userToken)
                    stopUpdateLocation()
                }
            }
        }

        jobByGame[promiseCode] = gameJob

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeground(promiseAlarm: PromiseAlarm) {
        if (jobByGame.values.isNotEmpty()) {
            return
        }

        val intent = Intent(this, GamingActivity::class.java).apply {
            putPromiseAlarm(promiseAlarm)
        }

        val pendingIntent: PendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                promiseAlarm.alarmCode,
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
        startForeground(promiseAlarm.alarmCode, notification)
    }

    private fun stopUpdateLocation() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun extractTimeDifference(startTime: OffsetDateTime, endTime: OffsetDateTime) =
        ((endTime.toEpochSecond() - startTime.toEpochSecond()) / 60).toInt()

    private fun stopGame(promiseCode: String) {
        jobByGame.run {
            get(promiseCode)?.cancel()
            remove(promiseCode)
        }

        if (jobByGame.values.isEmpty()) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopUpdateLocation()
    }

    companion object {
        private const val LOCATION_UPDATE_SECOND_INTERVAL = 20L
        private const val MAGNETIC_FIELD_UPDATE_SECOND_INTERVAL = 30
        private const val INITIAL_MAGNETIC_FIELD_RADIUS = 10000.0
        private const val INITIAL_GAME_TIME = 1
    }
}