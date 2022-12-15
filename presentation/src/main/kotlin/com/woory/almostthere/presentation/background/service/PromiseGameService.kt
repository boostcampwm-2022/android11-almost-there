package com.woory.almostthere.presentation.background.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.location.Location
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
import com.woory.almostthere.data.repository.PromiseRepository
import com.woory.almostthere.data.repository.UserRepository
import com.woory.almostthere.data.util.MAGNETIC_FIELD_UPDATE_TERM_SECOND
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.background.notification.NotificationChannelProvider
import com.woory.almostthere.presentation.background.notification.NotificationProvider
import com.woory.almostthere.presentation.background.util.asPromiseAlarm
import com.woory.almostthere.presentation.background.util.putPromiseAlarm
import com.woory.almostthere.presentation.extension.repeatOnStarted
import com.woory.almostthere.presentation.model.GeoPoint
import com.woory.almostthere.presentation.model.MagneticInfo
import com.woory.almostthere.presentation.model.PromiseAlarm
import com.woory.almostthere.presentation.model.UserLocation
import com.woory.almostthere.presentation.model.mapper.location.asDomain
import com.woory.almostthere.presentation.model.mapper.magnetic.asUiModel
import com.woory.almostthere.presentation.model.mapper.promise.asUiModel
import com.woory.almostthere.presentation.ui.gaming.GamingActivity
import com.woory.almostthere.presentation.util.DistanceUtil
import com.woory.almostthere.presentation.util.TimeConverter.asMillis
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
        INITIAL_MAGNETIC_FIELD_RADIUS
    )
    private val magneticZoneInitialRadius: StateFlow<Double> =
        _magneticZoneInitialRadius.asStateFlow()

    private val _gameTimeInitialValue: MutableStateFlow<Int> = MutableStateFlow(INITIAL_GAME_TIME)
    private val gameTimeInitialValue: StateFlow<Int> = _gameTimeInitialValue.asStateFlow()

    private val _magneticZoneInfo: MutableStateFlow<MagneticInfo?> = MutableStateFlow(null)
    private val magneticZoneInfo: StateFlow<MagneticInfo?> = _magneticZoneInfo.asStateFlow()

    private val _location: MutableStateFlow<GeoPoint?> = MutableStateFlow(null)
    val location: StateFlow<GeoPoint?> = _location.asStateFlow()

    private val _userHp: MutableStateFlow<Int> = MutableStateFlow(-1)
    private val userHp: StateFlow<Int> = _userHp.asStateFlow()

    private val _magneticZoneRadius: MutableStateFlow<Double> = MutableStateFlow(0.0)
    private val magneticZoneRadius: StateFlow<Double> = _magneticZoneRadius.asStateFlow()

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            val loc = p0.lastLocation
            loc?.let { location ->
                onUpdateLocation(location)
            }
        }
    }

    private fun onUpdateLocation(location: Location) {
        lifecycleScope.launch {
            val userLocation = GeoPoint(location.latitude, location.longitude)
            userId.value?.let { id ->
                setUserLocation(id, userLocation)
                updateHp(id, userLocation)
            }
        }
    }

    private fun updateHp(userToken: String, userLocation: GeoPoint) {
        lifecycleScope.launch {
            magneticZoneInfo.value?.let {
                if (magneticZoneRadius.value != 0.0 && DistanceUtil.getDistance(
                        it.centerPoint,
                        userLocation
                    ) > magneticZoneRadius.value
                ) {
                    _userHp.value -= 1
                    promiseRepository.decreaseUserHp(it.gameCode, userToken, userHp.value)
                        .onSuccess { updatedHp ->
                            if (updatedHp <= 0L) {
                                stopUpdateLocation()
                            }
                        }
                }
            }
        }
    }

    private fun setUserLocation(userToken: String, userLocation: GeoPoint) {
        lifecycleScope.launch {
            promiseRepository.setUserLocation(
                UserLocation(userToken, userLocation, System.currentTimeMillis()).asDomain()
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()

        repeatOnStarted {
            userRepository.userPreferences.collectLatest {
                _userId.emit(it.userID)
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

        setIsStartedGame(promiseCode)

        val gameJob = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    promiseRepository.updateInitialMagneticRadius(promiseCode)
                }

                val userToken = userId.value ?: return@repeatOnLifecycle

                promiseRepository.checkReEntryOfGame(promiseCode, userToken).onSuccess {
                    when (it) {
                        true -> {
                            setInitialHpDate(promiseCode, userToken)
                        }
                        false -> {
                            kickOutFromGame(promiseCode, userToken)
                        }
                    }
                }.onFailure {
                    kickOutFromGame(promiseCode, userToken)
                }
            }
        }

        jobByGame[promiseCode] = gameJob

        return super.onStartCommand(intent, flags, startId)
    }

    private fun setInitialHpDate(promiseCode: String, userToken: String) {
        lifecycleScope.launch {
            promiseRepository.setUserInitialHpData(promiseCode, userToken)
                .onSuccess {
                    _userHp.emit(it)
                    fetchInitialMagneticInfo(promiseCode, userToken)
                }
        }
    }

    private fun fetchInitialMagneticInfo(promiseCode: String, userToken: String) {
        lifecycleScope.launch {
            promiseRepository.getMagneticInfoByCode(promiseCode)
                .onSuccess { magneticModel ->
                    val magneticUiModel = magneticModel.asUiModel()

                    _magneticZoneInfo.emit(magneticUiModel)
                    _magneticZoneInitialRadius.emit(magneticUiModel.initialRadius)
                    _magneticZoneRadius.emit(magneticUiModel.radius)

                    fetchInitialPromiseInfo(promiseCode, userToken)
                }
        }
    }

    private fun fetchInitialPromiseInfo(promiseCode: String, userToken: String) {
        lifecycleScope.launch {
            promiseRepository.getPromiseByCode(promiseCode)
                .onSuccess { promiseModel ->
                    val promiseUiModel = promiseModel.asUiModel()

                    if (promiseUiModel.data.gameDateTime.asMillis() - System.currentTimeMillis() > 20 * 1000) {
                        kickOutFromGame(promiseCode, userToken)
                    } else {
                        _gameTimeInitialValue.emit(
                            extractTimeDifference(
                                promiseUiModel.data.gameDateTime,
                                promiseUiModel.data.promiseDateTime
                            )
                        )

                        fetchPlayerArrival(promiseCode, userToken)
                        fetchPlayerFinished(promiseCode)
                        updateMagneticInfo(promiseCode)
                    }
                }
        }
    }

    private fun kickOutFromGame(promiseCode: String, userToken: String) {
        lifecycleScope.launch {
            promiseRepository.sendOutUser(promiseCode, userToken)
            stopUpdateLocation()
        }
    }

    private fun updateMagneticInfo(promiseCode: String) {
        lifecycleScope.launch {
            while (true) {
                delay((1000 * MAGNETIC_FIELD_UPDATE_TERM_SECOND).toLong())

                _magneticZoneRadius.value -= magneticZoneInitialRadius.value / gameTimeInitialValue.value
                promiseRepository.decreaseMagneticRadius(
                    promiseCode,
                    magneticZoneRadius.value
                )
            }
        }
    }

    private fun fetchPlayerFinished(promiseCode: String) {
        lifecycleScope.launch {
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
    }

    private fun fetchPlayerArrival(promiseCode: String, userToken: String) {
        lifecycleScope.launch {
            promiseRepository.getPlayerArrived(
                promiseCode,
                userToken
            ).collectLatest { arrivedResult ->
                if (arrivedResult.isSuccess && arrivedResult.getOrDefault(false)
                ) {
                    stopUpdateLocation()
                }
            }
        }
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

    private fun setIsStartedGame(promiseCode: String) {
        lifecycleScope.launch {
            promiseRepository.setIsStartedGame(promiseCode)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopUpdateLocation()
    }

    companion object {
        private const val LOCATION_UPDATE_SECOND_INTERVAL = 20L
        private const val INITIAL_MAGNETIC_FIELD_RADIUS = 10000.0
        private const val INITIAL_GAME_TIME = 1
    }
}