package com.woory.presentation.ui.creatingpromise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.data.repository.UserRepository
import com.woory.presentation.model.Color
import com.woory.presentation.model.Location
import com.woory.presentation.model.ProfileImage
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
    val readyDuration: StateFlow<Duration?> = _readyDuration.asStateFlow()

    val isEnabled: Flow<Boolean> = combine(
        promiseLocation,
        promiseDate,
        promiseTime,
        readyDuration
    ) { _promiseLocation, _promiseDate, _promiseTime, _gameTime ->
        (_promiseLocation != null) && (_promiseDate != null) && (_promiseTime != null) && (_gameTime != null)
    }

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

    fun setPromise() {
        viewModelScope.launch {
            userRepository.userPreferences.collectLatest { userPreferences ->
                val name = nickname.value
                val profileImage = UserProfileImage(
                    profileImageBackgroundColor.value.toString(),
                    profileImageIndex.value
                )
                val promiseLocation = _promiseLocation.value ?: return@collectLatest
                val promiseDate = _promiseDate.value ?: return@collectLatest
                val promiseTime = _promiseTime.value ?: return@collectLatest
                val readyDuration = _readyDuration.value ?: return@collectLatest

                val zoneOffset = OffsetDateTime.now().offset
                val promiseDateTime = OffsetDateTime.of(promiseDate, promiseTime, zoneOffset)
                val gameDateTime =
                    OffsetDateTime.of(promiseDate, promiseTime, zoneOffset).minus(readyDuration)

                val user = User(userPreferences.userID, UserData(name, profileImage))

                val promiseData =
                    PromiseData(promiseLocation, promiseDateTime, gameDateTime, user, listOf(user))

                promiseRepository.setPromise(promiseData.asDomain()).onSuccess { promiseCode ->
                    promiseRepository.getPromiseAlarm(promiseCode).onSuccess { promiseAlarm ->
                        _promiseSettingEvent.emit(promiseAlarm.asUiModel())
                    }
                }
            }
        }
    }

    fun searchLocation(query: String) {
        viewModelScope.launch {
            promiseRepository.getSearchedLocationByKeyword(query).onSuccess {
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