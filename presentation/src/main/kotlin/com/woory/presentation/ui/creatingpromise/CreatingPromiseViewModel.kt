package com.woory.presentation.ui.creatingpromise


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.model.*
import com.woory.presentation.model.mapper.alarm.asUiModel
import com.woory.presentation.model.mapper.promise.asDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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

    private val _userProfileImage: MutableStateFlow<UserProfileImage> = MutableStateFlow(
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

    fun setUser() {
        viewModelScope.launch {
            val name = _userName.value ?: return@launch
            val profileImage = _userProfileImage.value ?: return@launch
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

    fun createPromise() {
        TODO("약속 생성하여 전달")
    }
}