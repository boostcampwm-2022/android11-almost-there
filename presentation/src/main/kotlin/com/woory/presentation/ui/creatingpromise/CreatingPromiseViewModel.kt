package com.woory.presentation.ui.creatingpromise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.model.Location
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.model.PromiseData
import com.woory.presentation.model.User
import com.woory.presentation.model.UserData
import com.woory.presentation.model.UserProfileImage
import com.woory.presentation.model.mapper.alarm.asUiModel
import com.woory.presentation.model.mapper.promise.asDomain
import com.woory.presentation.model.mapper.searchlocation.SearchResultMapper
import com.woory.presentation.ui.promiseinfo.PromiseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class CreatingPromiseViewModel @Inject constructor(private val repository: PromiseRepository) :
    ViewModel() {

    // TODO("UserName, UserProfileImage default 값으로 주어짐, 연결 필요")
    private val _userName: MutableStateFlow<String?> = MutableStateFlow("anonymous")
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private val _userProfileImage: MutableStateFlow<UserProfileImage?> = MutableStateFlow(
        UserProfileImage("0xffffff", 1)
    )
    val userProfileImage: StateFlow<UserProfileImage?> = _userProfileImage.asStateFlow()

    private val _promiseLocation: MutableStateFlow<Location?> = MutableStateFlow(null)
    val promiseLocation: StateFlow<Location?> = _promiseLocation.asStateFlow()

    private val _promiseDate: MutableStateFlow<LocalDate?> = MutableStateFlow(null)
    val promiseDate: StateFlow<LocalDate?> = _promiseDate.asStateFlow()

    private val _promiseTime: MutableStateFlow<LocalTime?> = MutableStateFlow(null)
    val promiseTime: StateFlow<LocalTime?> = _promiseTime.asStateFlow()

    private val _readyDuration: MutableStateFlow<Duration?> = MutableStateFlow(null)
    val readyDuration: StateFlow<Duration?> = _readyDuration.asStateFlow()

    val isEnabled: Flow<Boolean> = combine(
        promiseLocation,
        promiseDate,
        promiseTime,
        readyDuration
    ) { _promiseLocation, _promiseDate, _promiseTime, _gameTime ->
        (_promiseLocation != null) && (_promiseDate != null) && (_promiseTime != null) && (_gameTime != null)
    }

    private val _userSettingEvent: MutableSharedFlow<Unit> = MutableSharedFlow()
    val userSettingEvent: SharedFlow<Unit> = _userSettingEvent.asSharedFlow()

    private val _promiseSettingEvent: MutableSharedFlow<PromiseAlarm> = MutableSharedFlow()
    val promiseSettingEvent: SharedFlow<PromiseAlarm> = _promiseSettingEvent.asSharedFlow()

    private val _locationSearchResult: MutableStateFlow<List<LocationSearchResult>> =
        MutableStateFlow(
            listOf()
        )
    val locationSearchResult: StateFlow<List<LocationSearchResult>> =
        _locationSearchResult.asStateFlow()

    private val _choosedLocation: MutableStateFlow<Location?> = MutableStateFlow(null)
    val choosedLocation: StateFlow<Location?> = _choosedLocation.asStateFlow()

    private val _locationSearchUiState: MutableStateFlow<PromiseUiState> =
        MutableStateFlow(PromiseUiState.Loading)
    val locationSearchUiState: StateFlow<PromiseUiState> = _locationSearchUiState.asStateFlow()

    fun setUser() {
        viewModelScope.launch {
            _userSettingEvent.emit(Unit)
        }
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

    fun setPromise() {
        viewModelScope.launch {
            val name = _userName.value ?: return@launch
            val profileImage = _userProfileImage.value ?: return@launch
            val promiseLocation = _promiseLocation.value ?: return@launch
            val promiseDate = _promiseDate.value ?: return@launch
            val promiseTime = _promiseTime.value ?: return@launch
            val readyDuration = _readyDuration.value ?: return@launch

            val zoneOffset = OffsetDateTime.now().offset
            val promiseDateTime = OffsetDateTime.of(promiseDate, promiseTime, zoneOffset)
            val gameDateTime =
                OffsetDateTime.of(promiseDate, promiseTime, zoneOffset).minus(readyDuration)

            // TODO("User Token 처리 필요")
            val userToken = "testCode"
            val user = User(userToken, UserData(name, profileImage))

            val promiseData =
                PromiseData(promiseLocation, promiseDateTime, gameDateTime, user, listOf(user))

            repository.setPromise(promiseData.asDomain()).onSuccess { promiseCode ->
                repository.getPromiseAlarm(promiseCode).onSuccess { promiseAlarm ->
                    _promiseSettingEvent.emit(promiseAlarm.asUiModel())
                }
            }
        }
    }

    fun searchLocation(query: String) {
        viewModelScope.launch {
            repository.getSearchedLocationByKeyword(query).onSuccess {
                setSearchedResult(it.map { SearchResultMapper.asUiModel(it) })
            }
        }
    }

    suspend fun setSearchedResult(lst: List<LocationSearchResult>) {
        _locationSearchResult.emit(lst)
    }

    fun chooseLocation(location: Location) {
        viewModelScope.launch {
            _choosedLocation.emit(location)
        }
    }
}