package com.woory.presentation.ui.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.data.repository.UserRepository
import com.woory.presentation.model.*
import com.woory.presentation.model.mapper.alarm.asUiModel
import com.woory.presentation.model.mapper.promise.asDomain
import com.woory.presentation.model.mapper.promise.asUiModel
import com.woory.presentation.model.mapper.user.asDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository,
    private val userRepository: UserRepository
//    @ProfileModule.Code private val code: String
) : ViewModel() {

    val profileImageBackgroundColor: MutableStateFlow<Color> = MutableStateFlow(Color(0, 0, 0))

    val profileImageIndex: MutableStateFlow<Int> = MutableStateFlow(0)

    val nickname: MutableStateFlow<String> = MutableStateFlow("")

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorType: MutableStateFlow<CodeState?> = MutableStateFlow(null)
    val errorType: StateFlow<CodeState?> = _errorType.asStateFlow()

    private val _promise: MutableStateFlow<Promise?> = MutableStateFlow(null)
    val promise: StateFlow<Promise?> = _promise.asStateFlow()

    private val _promiseSettingEvent: MutableSharedFlow<PromiseAlarm> = MutableSharedFlow()
    val promiseSettingEvent: SharedFlow<PromiseAlarm> = _promiseSettingEvent.asSharedFlow()

    private val _error: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error.asStateFlow()

    private val _success: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success.asStateFlow()

    private val userPreferences = userRepository.userPreferences

    fun shuffleProfileImage() {
        profileImageIndex.value = ProfileImage.getRandomImage()
        profileImageBackgroundColor.value = Color.getRandomColor()
    }

    fun join(code: String) {
        viewModelScope.launch {
            _isLoading.value = true

            promiseRepository.getPromiseAlarm(code)
                .onSuccess {
                    _isLoading.value = false
                    _errorType.value = CodeState.ALREADY_JOIN
                }
                .onFailure {
                    promiseRepository.getPromiseByCode(code)
                        .onSuccess { promise ->
                            _isLoading.value = false
                            _promise.value = promise.asUiModel()
                        }
                        .onFailure {
                            _isLoading.value = false
                            _errorType.value = CodeState.NONEXISTENT
                        }
                }
        }
    }

    fun insertPromise(promise: Promise) {
        viewModelScope.launch {
            _isLoading.value = true

            promiseRepository.setPromiseAlarmByPromiseModel(promise.asDomain())
                .onSuccess {
                    addPlayer(promise.code)
                }
                .onFailure {
                    _isLoading.value = false
                    _error.value = true
                }
        }
    }

    private fun addPlayer(code: String) {
        viewModelScope.launch {
            userPreferences.collectLatest { userPreferences ->
                val user = User(
                    userId = userPreferences.userID,
                    data = UserData(
                        name = nickname.value,
                        profileImage = UserProfileImage(
                            color = profileImageBackgroundColor.value.toString(),
                            imageIndex = profileImageIndex.value
                        )
                    )
                )

                promiseRepository.addPlayer(code = code, user = user.asDomain())
                    .onSuccess {
                        _isLoading.value = false
                        _success.value = true
                    }
                    .onFailure {
                        _isLoading.value = false
                        _error.value = true
                    }
            }
        }
    }

    fun setPromiseAlarm() {
        viewModelScope.launch {
            val promise = promise.value
            promise ?: return@launch

            promiseRepository.setPromiseAlarmByPromiseModel(promise.asDomain()).onSuccess {
                promiseRepository.getPromiseAlarm(promise.code).onSuccess { promiseAlarmModel ->
                    _promiseSettingEvent.emit(promiseAlarmModel.asUiModel())
                }
            }
        }
    }
}