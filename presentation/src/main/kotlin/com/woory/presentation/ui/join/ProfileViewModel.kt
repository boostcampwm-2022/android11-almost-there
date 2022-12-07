package com.woory.presentation.ui.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.model.PromiseModel
import com.woory.data.repository.PromiseRepository
import com.woory.data.repository.UserRepository
import com.woory.presentation.model.Color
import com.woory.presentation.model.ProfileImage
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.model.UiState
import com.woory.presentation.model.User
import com.woory.presentation.model.UserData
import com.woory.presentation.model.UserProfileImage
import com.woory.presentation.model.exception.AlmostThereException
import com.woory.presentation.model.mapper.alarm.asUiModel
import com.woory.presentation.model.mapper.user.asDomain
import com.woory.presentation.util.flow.EventFlow
import com.woory.presentation.util.flow.MutableEventFlow
import com.woory.presentation.util.flow.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val profileImageBackgroundColor: MutableStateFlow<Color> = MutableStateFlow(Color(0, 0, 0))

    val profileImageIndex: MutableStateFlow<Int> = MutableStateFlow(0)

    val nickname: MutableStateFlow<String> = MutableStateFlow("")

    private val _uiState: MutableEventFlow<UiState<PromiseAlarm>> = MutableEventFlow()
    val uiState: EventFlow<UiState<PromiseAlarm>> = _uiState.asEventFlow()

    private val userPreferences = userRepository.userPreferences

    fun shuffleProfileImage() {
        profileImageIndex.value = ProfileImage.getRandomImage()
        profileImageBackgroundColor.value = Color.getRandomColor()
    }

    fun join(code: String) {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)

            promiseRepository.getPromiseAlarm(code)
                .onSuccess {
                    _uiState.emit(UiState.Error(AlmostThereException.AlreadyJoinedPromiseException()))
                }
                .onFailure {
                    promiseRepository.getPromiseByCode(code)
                        .onSuccess { promise ->
                            if (OffsetDateTime.now() >= promise.data.gameDateTime) {
                                _uiState.emit(UiState.Error(AlmostThereException.AlreadyStartedPromiseException()))
                            } else {
                                insertPromise(promise)
                            }
                        }
                        .onFailure {
                            _uiState.emit(UiState.Error(AlmostThereException.InvalidCodeException()))
                        }
                }
        }
    }

    private fun insertPromise(promiseModel: PromiseModel) {
        viewModelScope.launch {
            promiseRepository.setPromiseAlarmByPromiseModel(promiseModel)
                .onSuccess {
                    addPlayer(promiseModel)
                }
                .onFailure {
                    _uiState.emit(UiState.Error(AlmostThereException.StoreFailedException()))
                }
        }
    }

    private fun addPlayer(promiseModel: PromiseModel) {
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

                promiseRepository.addPlayer(code = promiseModel.code, user = user.asDomain())
                    .onSuccess {
                        getPromiseAlarm(promiseModel)
                    }
                    .onFailure {
                        _uiState.emit(UiState.Error(AlmostThereException.StoreFailedException()))
                    }
            }
        }
    }

    private fun getPromiseAlarm(promiseModel: PromiseModel) {
        viewModelScope.launch {
            promiseRepository.getPromiseAlarm(promiseModel.code)
                .onSuccess { promiseAlarmModel ->
                    _uiState.emit(UiState.Success(promiseAlarmModel.asUiModel()))
                }
                .onFailure {
                    _uiState.emit(UiState.Error(AlmostThereException.FetchFailedException()))
                }
        }
    }
}