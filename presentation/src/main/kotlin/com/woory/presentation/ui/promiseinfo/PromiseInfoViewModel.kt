package com.woory.presentation.ui.promiseinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.model.Promise
import com.woory.presentation.model.mapper.promise.PromiseMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PromiseInfoViewModel @Inject constructor(
    private val repository: PromiseRepository
) : ViewModel() {
    private val _gameCode: MutableStateFlow<String> = MutableStateFlow("")
    val gameCode: StateFlow<String> = _gameCode.asStateFlow()

    private val _uiState: MutableStateFlow<PromiseUiState> =
        MutableStateFlow(PromiseUiState.Waiting)
    val uiState: StateFlow<PromiseUiState> = _uiState.asStateFlow()

    private val _promiseModel: MutableStateFlow<Promise?> = MutableStateFlow(null)
    val promiseModel: StateFlow<Promise?> = _promiseModel.asStateFlow()

    private val _host: MutableStateFlow<String> = MutableStateFlow("")
    private val host: StateFlow<String> = _host.asStateFlow()

    fun setGameCode(code: String) {
        viewModelScope.launch {
            _gameCode.emit(code)
        }
    }

    fun setUiState(state: PromiseUiState) {
        viewModelScope.launch {
            _uiState.emit(state)
        }
    }

    fun isHost(userId: String): Boolean {
        return host.value == userId
    }

    fun fetchPromiseDate() {
        viewModelScope.launch {
            val code = gameCode.value
            _uiState.emit(PromiseUiState.Loading)

            repository.getPromiseByCodeAndListen(code).collect {
                it.onSuccess {
                    it.data.users.forEach {
                        Timber.tag("123123").d(it.data.profileImage.toString())
                    }
                    val promiseModel = PromiseMapper.asUiModel(it)
                    _uiState.emit(PromiseUiState.Success)
                    _promiseModel.emit(promiseModel)
                    _host.emit(promiseModel.data.host.userId)
                }
                it.onFailure { error ->
                    setUiState(PromiseUiState.Fail(error.message ?: DEFAULT_ERROR_MESSAGE))
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = ""
    }
}