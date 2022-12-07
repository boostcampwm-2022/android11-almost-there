package com.woory.presentation.ui.promiseinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.data.repository.RouteRepository
import com.woory.data.repository.UserRepository
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.Promise
import com.woory.presentation.model.UserLocation
import com.woory.presentation.model.mapper.location.asDomain
import com.woory.presentation.model.mapper.promise.PromiseMapper
import com.woory.presentation.util.TimeConverter.asMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
    private val _gameCode: MutableStateFlow<String> = MutableStateFlow("")
    val gameCode: StateFlow<String> = _gameCode.asStateFlow()

    private val _uiState: MutableStateFlow<PromiseUiState> =
        MutableStateFlow(PromiseUiState.Success)
    val uiState: StateFlow<PromiseUiState> = _uiState.asStateFlow()

    private val _errorState: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errorState: SharedFlow<Throwable> = _errorState.asSharedFlow()

    private val _promiseModel: MutableStateFlow<Promise?> = MutableStateFlow(null)
    val promiseModel: StateFlow<Promise?> = _promiseModel.asStateFlow()

    private val _host: MutableStateFlow<String> = MutableStateFlow("")
    private val host: StateFlow<String> = _host.asStateFlow()

    private val _gameTime: MutableStateFlow<String> = MutableStateFlow("")
    val gameTime: StateFlow<String> = _gameTime.asStateFlow()

    private val _promiseTime: MutableStateFlow<String> = MutableStateFlow("")
    val promiseTime: StateFlow<String> = _promiseTime.asStateFlow()

    fun setGameCode(code: String) {
        viewModelScope.launch {
            _gameCode.emit(code)
        }
    }

    fun isHost(userId: String): Boolean = host.value == userId

    fun fetchPromiseDate() {
        viewModelScope.launch {
            val code = gameCode.value
            _uiState.emit(PromiseUiState.Loading)

            promiseRepository.getPromiseByCodeAndListen(code).collect {
                it.onSuccess {
                    val promiseModel = PromiseMapper.asUiModel(it)

                    _gameTime.emit(
                        promiseModel.data.gameDateTime.format(
                            DateTimeFormatter.ofPattern(
                                "yyyy.MM.dd hh:mm"
                            )
                        )
                    )
                    _promiseTime.emit(
                        promiseModel.data.promiseDateTime.format(
                            DateTimeFormatter.ofPattern(
                                "yyyy.MM.dd hh:mm"
                            )
                        )
                    )

                    _uiState.emit(PromiseUiState.Success)
                    _promiseModel.emit(promiseModel)
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
                        .onFailure { throwable ->
                            _uiState.emit(PromiseUiState.Fail)
                            _errorState.emit(throwable)
                        }
                }
                .onFailure { throwable ->
                    _uiState.emit(PromiseUiState.Fail)
                    _errorState.emit(throwable)
                }
        }
    }
}