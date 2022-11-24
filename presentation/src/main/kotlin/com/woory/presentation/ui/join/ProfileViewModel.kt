package com.woory.presentation.ui.join

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.model.GameTimeInfoModel
import com.woory.data.model.UserImage
import com.woory.data.model.UserModel
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.model.Color
import com.woory.presentation.model.PromiseDataModel
import com.woory.presentation.model.mapper.asDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository
//    @ProfileModule.Code private val code: String
) : ViewModel() {

    val profileImageBackgroundColor: MutableStateFlow<Color> = MutableStateFlow(Color(0, 0, 0))

    val profileImageIndex: MutableStateFlow<Int> = MutableStateFlow(0)

    val nickname: MutableStateFlow<String> = MutableStateFlow("")

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorType: MutableStateFlow<CodeState?> = MutableStateFlow(null)
    val errorType: StateFlow<CodeState?> = _errorType.asStateFlow()

    private val _promise: MutableStateFlow<PromiseDataModel?> = MutableStateFlow(null)
    val promise: StateFlow<PromiseDataModel?> = _promise.asStateFlow()

    private val _error: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val error: StateFlow<Boolean> = _error.asStateFlow()

    private val _success: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success.asStateFlow()

    private val images = ProfileImage.values()

    fun shuffleProfileImage() {
        profileImageIndex.value = images.indices.random()
        profileImageBackgroundColor.value = Color.getRandomColor()
    }

    fun join(code: String) {
        viewModelScope.launch {
            _isLoading.value = true

            promiseRepository.getGameTimeByCode(code)
                .onSuccess {
                    _isLoading.value = false
                    _errorType.value = CodeState.ALREADY_JOIN
                }
                .onFailure {
                    promiseRepository.getPromiseByCode(code)
                        .onSuccess { promise ->
                            _isLoading.value = false
                            _promise.value = promise.asDomain()
                        }
                        .onFailure {
                            _isLoading.value = false
                            _errorType.value = CodeState.NONEXISTENT
                        }
                }
        }
    }

    fun insertPromise(promise: PromiseDataModel) {
        viewModelScope.launch {
            _isLoading.value = true

            promiseRepository.insertPromise(
                GameTimeInfoModel(
                    code = promise.code,
                    startTime = promise.gameDateTime,
                    endTime = promise.promiseDateTime
                )
            )
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
        val user = UserModel(
            userId = UUID.randomUUID().toString(),
            userName = nickname.value,
            userImage = UserImage(
                color = profileImageBackgroundColor.value.toString(),
                imageIdx = profileImageIndex.value
            )
        )

        viewModelScope.launch {
            promiseRepository.addPlayer(code = code, user = user)
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