package com.woory.presentation.ui.promiseinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.data.repository.RouteRepository
import com.woory.data.repository.UserRepository
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.Promise
import com.woory.presentation.model.User
import com.woory.presentation.model.UserLocation
import com.woory.presentation.model.mapper.location.asDomain
import com.woory.presentation.model.mapper.promise.PromiseMapper
import com.woory.presentation.util.TimeConverter.asMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PromiseInfoViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository,
    private val userRepository: UserRepository,
    private val routeRepository: RouteRepository,
) : ViewModel() {
    private var timerJob: Job? = null

    private val _gameCode: MutableStateFlow<String> = MutableStateFlow("")
    val gameCode: StateFlow<String> = _gameCode.asStateFlow()

    private val _uiState: MutableStateFlow<PromiseUiState> =
        MutableStateFlow(PromiseUiState.Loading)
    val uiState: StateFlow<PromiseUiState> = _uiState.asStateFlow()

    private val _errorState: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errorState: SharedFlow<Throwable> = _errorState.asSharedFlow()

    private val _promiseModel: MutableStateFlow<Promise?> = MutableStateFlow(null)
    val promiseModel: StateFlow<Promise?> = _promiseModel.asStateFlow()

    private val _users: MutableStateFlow<List<User>> = MutableStateFlow(listOf())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _host: MutableStateFlow<String> = MutableStateFlow("")
    private val host: StateFlow<String> = _host.asStateFlow()

    private val _gameTime: MutableStateFlow<String> = MutableStateFlow("")
    val gameTime: StateFlow<String> = _gameTime.asStateFlow()

    private val _promiseTime: MutableStateFlow<String> = MutableStateFlow("")
    val promiseTime: StateFlow<String> = _promiseTime.asStateFlow()

    private val _isUserReady: MutableStateFlow<ReadyStatus> = MutableStateFlow(ReadyStatus.NOT)
    val isUserReady: StateFlow<ReadyStatus> = _isUserReady

    private val _readyUsers: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    val readyUsers: StateFlow<List<String>> = _readyUsers

    private val _isAvailSetAlarm: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val  isAvailSetAlarm: StateFlow<Boolean> = _isAvailSetAlarm

    private var _blockReady: Boolean = false
    val blockReady get() = _blockReady

    fun setGameCode(code: String) {
        viewModelScope.launch {
            _gameCode.emit(code)
        }
    }

    fun isHost(userId: String): Boolean = host.value == userId

    fun fetchReadyUsers() {
        viewModelScope.launch {
            val code = gameCode.value
            promiseRepository.getReadyUsers(code).collectLatest { result ->
                result.onSuccess {
                    _readyUsers.emit(it)
                }
            }
        }
    }

    fun fetchPromiseDate() {
        viewModelScope.launch {
            val code = gameCode.value
            _uiState.emit(PromiseUiState.Loading)

            promiseRepository.getPromiseByCodeAndListen(code).collectLatest {
                it.onSuccess {
                    val promiseModel = PromiseMapper.asUiModel(it)

                    _gameTime.emit(
                        promiseModel.data.gameDateTime.format(
                            DateTimeFormatter.ofPattern(
                                "yyyy.MM.dd a hh:mm"
                            )
                        )
                    )
                    _promiseTime.emit(
                        promiseModel.data.promiseDateTime.format(
                            DateTimeFormatter.ofPattern(
                                "yyyy.MM.dd a hh:mm"
                            )
                        )
                    )

                    _uiState.emit(PromiseUiState.Success)
                    _promiseModel.emit(promiseModel)
                    _users.emit(promiseModel.data.users)

                    checkAvailReadyButton()
                    fetchUserReady()
                    _host.emit(promiseModel.data.host.userId)
                }
                it.onFailure { throwable ->
                    _uiState.emit(PromiseUiState.Fail)
                    _errorState.emit(throwable)
                }
            }
        }
    }

    fun setUserCurrentLocation(startGeoPoint: GeoPoint) {
        viewModelScope.launch {
            userRepository.userPreferences.collectLatest {
                val userToken = it.userID
                val currentUserLocation =
                    UserLocation(userToken, startGeoPoint, OffsetDateTime.now().asMillis())
                promiseRepository.setUserLocation(currentUserLocation.asDomain())
                    .onFailure { throwable ->
                        _uiState.emit(PromiseUiState.Fail)
                        _errorState.emit(throwable)
                    }
            }
        }
    }

    fun setPromiseMagneticRadius(startGeoPoint: GeoPoint) {
        viewModelScope.launch {
            _blockReady = true
            _uiState.emit(PromiseUiState.Loading)
            val promise = promiseModel.value ?: return@launch

            val gameDuration =
                Duration.between(promise.data.gameDateTime, promise.data.promiseDateTime)
                    .toMinutes()
            val destGeoPoint = promiseModel.value?.data?.promiseLocation?.geoPoint ?: return@launch

            routeRepository.getMaximumVelocity(
                startGeoPoint.asDomain(),
                destGeoPoint.asDomain()
            )
                .onSuccess { velocity ->
                    val maxRadius = velocity * gameDuration
                    promiseRepository.updateMagneticRadius(promise.code, maxRadius)
                        .onSuccess {
                            setUserReady()
                            _isAvailSetAlarm.emit(true)
                            _uiState.emit(PromiseUiState.Success)
                        }
                        .onFailure { _ ->
                            _uiState.emit(PromiseUiState.Fail)
                            _errorState.emit(IllegalArgumentException("네트워크를 확인해주세요."))
                        }
                }
                .onFailure { throwable ->
                    _uiState.emit(PromiseUiState.Fail)
                    _errorState.emit(throwable)
                }
        }
    }

    suspend fun getPromiseAlarmByCode(promiseCode: String) =
        promiseRepository.getPromiseAlarm(promiseCode)


    private fun setUserReady() {
        viewModelScope.launch {
            val promise = promiseModel.value ?: return@launch
            val userId = runBlocking { userRepository.userPreferences.first().userID }
            promiseRepository.setUserReady(promise.code, userId)
                .onFailure { throwable ->
                    _uiState.emit(PromiseUiState.Fail)
                    _errorState.emit(throwable)
                }
                .also {
                    _blockReady = false
                }
        }
    }

    private fun fetchUserReady() {
        viewModelScope.launch {
            val promise = promiseModel.value ?: return@launch
            val userId = runBlocking { userRepository.userPreferences.first().userID }
            promiseRepository.getIsReadyUser(promise.code, userId).collectLatest { result ->
                result.onSuccess { isReady ->
                    if (isReady && _isUserReady.value == ReadyStatus.NOT) {
                        _isUserReady.emit(ReadyStatus.READY)
                    }
                }
                    .onFailure { throwable ->
                        _errorState.emit(throwable)
                    }
            }
        }
    }

    private fun checkAvailReadyButton() {
        val promise = promiseModel.value ?: return
        val gameStartTime = promise.data.gameDateTime.asMillis()
        val readyAvailTime = promise.data.gameDateTime.minusMinutes(5).asMillis()
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (true) {
                val currentTime = System.currentTimeMillis()
                when {
                    currentTime > gameStartTime -> {
                        _isUserReady.emit(ReadyStatus.AFTER)
                        timerJob?.cancel()
                        break
                    }
                    currentTime < readyAvailTime -> {
                        _isUserReady.emit(ReadyStatus.BEFORE)
                    }
                    _isUserReady.value != ReadyStatus.READY -> {
                        _isUserReady.emit(ReadyStatus.NOT)
                    }
                }
                delay(1000L)
            }
        }
    }
}