package com.woory.almostthere.presentation.ui.creatingpromise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.almostthere.data.model.UserPreferencesModel
import com.woory.almostthere.data.repository.PromiseRepository
import com.woory.almostthere.data.repository.UserRepository
import com.woory.almostthere.presentation.model.Color
import com.woory.almostthere.presentation.model.GeoPoint
import com.woory.almostthere.presentation.model.Location
import com.woory.almostthere.presentation.model.ProfileImage
import com.woory.almostthere.presentation.model.PromiseAlarm
import com.woory.almostthere.presentation.model.PromiseData
import com.woory.almostthere.presentation.model.User
import com.woory.almostthere.presentation.model.UserData
import com.woory.almostthere.presentation.model.UserProfileImage
import com.woory.almostthere.presentation.model.exception.InvalidGameTimeException
import com.woory.almostthere.presentation.model.mapper.alarm.asDomain
import com.woory.almostthere.presentation.model.mapper.alarm.asUiModel
import com.woory.almostthere.presentation.model.mapper.promise.asDomain
import com.woory.almostthere.presentation.util.TimeConverter.zoneOffset
import com.woory.almostthere.presentation.util.flow.EventFlow
import com.woory.almostthere.presentation.util.flow.MutableEventFlow
import com.woory.almostthere.presentation.util.flow.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class CreatingPromiseViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val profileImageBackgroundColor: MutableStateFlow<Color> = MutableStateFlow(Color(0, 0, 0))

    val profileImageIndex: MutableStateFlow<Int> = MutableStateFlow(0)

    val nickname: MutableStateFlow<String> = MutableStateFlow("")

    private val _promiseLocation: MutableStateFlow<Location?> = MutableStateFlow(null)
    val promiseLocation: StateFlow<Location?> = _promiseLocation.asStateFlow()

    private val _promiseDate: MutableStateFlow<LocalDate?> = MutableStateFlow(null)
    val promiseDate: StateFlow<LocalDate?> = _promiseDate.asStateFlow()

    private val _promiseTime: MutableStateFlow<LocalTime?> = MutableStateFlow(null)
    val promiseTime: StateFlow<LocalTime?> = _promiseTime.asStateFlow()

    private val _readyDuration: MutableStateFlow<Duration?> = MutableStateFlow(null)

    val isEnabled: Flow<Boolean> = combine(
        promiseLocation,
        promiseDate,
        promiseTime,
        _readyDuration
    ) { _promiseLocation, _promiseDate, _promiseTime, _gameTime ->
        (_promiseLocation != null) && (_promiseDate != null) && (_promiseTime != null) && (_gameTime != null)
    }

    private val _requestSetPromiseEvent: MutableEventFlow<PromiseData> = MutableEventFlow()
    val requestSetPromiseEvent: EventFlow<PromiseData> = _requestSetPromiseEvent.asEventFlow()

    private val _setPromiseSuccessEvent: MutableSharedFlow<PromiseAlarm> = MutableSharedFlow()
    val setPromiseSuccessEvent: SharedFlow<PromiseAlarm> = _setPromiseSuccessEvent.asSharedFlow()

    private val _choosedLocation: MutableStateFlow<GeoPoint?> = MutableStateFlow(null)
    val choosedLocation: StateFlow<GeoPoint?> = _choosedLocation.asStateFlow()

    private val _choosedLocationName: MutableStateFlow<String> = MutableStateFlow("")
    val choosedLocationName: StateFlow<String> = _choosedLocationName.asStateFlow()

    private val _uiState: MutableStateFlow<CreatingPromiseUiState> =
        MutableStateFlow(CreatingPromiseUiState.Success)
    val uiState: StateFlow<CreatingPromiseUiState> = _uiState.asStateFlow()

    private val _errorEvent: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errorEvent: SharedFlow<Throwable> = _errorEvent.asSharedFlow()

    private fun setStateLoading() {
        viewModelScope.launch {
            _uiState.emit(CreatingPromiseUiState.Loading)
        }
    }

    private fun setStateError(throwable: Throwable) {
        viewModelScope.launch {
            _uiState.emit(CreatingPromiseUiState.Success)
            _errorEvent.emit(throwable)
        }
    }

    fun setLocationName(name: String) {
        viewModelScope.launch {
            _choosedLocationName.emit(name)
        }
    }

    fun shuffleProfileImage() {
        profileImageIndex.value = ProfileImage.getRandomImage()
        profileImageBackgroundColor.value = Color.getRandomColor()
    }

    fun setPromiseLocation(location: Location?) {
        viewModelScope.launch {
            _promiseLocation.emit(location)
        }
    }

    fun setPromiseDate(date: LocalDate?) {
        viewModelScope.launch {
            _promiseDate.emit(date)
        }
    }

    fun setPromiseTime(time: LocalTime?) {
        viewModelScope.launch {
            _promiseTime.emit(time)
        }
    }

    fun setReadyDuration(duration: Duration?) {
        viewModelScope.launch {
            _readyDuration.emit(duration)
        }
    }

    private fun isNotEnabledPromiseDateTime(gameTime: OffsetDateTime): Boolean {
        val nowDateTime = OffsetDateTime.now()
        return nowDateTime.isAfter(gameTime.minusMinutes(MIN_DURATION_MINUTES))
    }

    private fun getPromiseData(userPreferencesModel: UserPreferencesModel): PromiseData? {
        val promiseLocation = _promiseLocation.value ?: return null
        val promiseDate = _promiseDate.value ?: return null
        val promiseTime = _promiseTime.value ?: return null
        val readyDuration = _readyDuration.value ?: return null

        val promiseDateTime =
            OffsetDateTime.of(promiseDate, promiseTime, zoneOffset)
        val gameDateTime =
            OffsetDateTime.of(promiseDate, promiseTime, zoneOffset)
                .minus(readyDuration)

        val name = nickname.value
        val profileImage = UserProfileImage(
            profileImageBackgroundColor.value.toString(),
            profileImageIndex.value
        )

        val user = User(userPreferencesModel.userID, UserData(name, profileImage))

        return PromiseData(promiseLocation, promiseDateTime, gameDateTime, user, listOf(user))
    }

    fun setRequestSetPromiseEvent() {
        viewModelScope.launch {
            userRepository.userPreferences.collectLatest {
                val promiseData = getPromiseData(it) ?: return@collectLatest
                _requestSetPromiseEvent.emit(promiseData)
            }
        }
    }

    fun setPromise() {
        viewModelScope.launch {
            userRepository.userPreferences.collectLatest {
                val promiseData = getPromiseData(it) ?: return@collectLatest

                if (isNotEnabledPromiseDateTime(promiseData.gameDateTime)) {
                    _errorEvent.emit(InvalidGameTimeException())
                    return@collectLatest
                }

                setStateLoading()

                promiseRepository.setPromise(promiseData.asDomain()).onSuccess { promiseCode ->
                    promiseRepository.getPromiseAlarm(promiseCode).onSuccess { promiseAlarm ->
                        _setPromiseSuccessEvent.emit(promiseAlarm.asUiModel())
                    }.onFailure { exception ->
                        setStateError(exception)
                    }
                }.onFailure { exception ->
                    setStateError(exception)
                }
            }
        }
    }

    fun setPromiseAlarm(promiseAlarm: PromiseAlarm) {
        viewModelScope.launch {
            promiseRepository.setPromiseAlarmByPromiseAlarmModel(promiseAlarm.asDomain())
        }
    }

    fun setChoosedLocation(getPoint: GeoPoint) {
        viewModelScope.launch {
            _choosedLocation.emit(getPoint)
        }
    }

    companion object {
        const val MIN_DURATION_MINUTES = 9L
    }
}