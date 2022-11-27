package com.woory.presentation.ui.promiseinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.model.Promise
import com.woory.presentation.model.mapper.promise.PromiseMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseInfoViewModel @Inject constructor(
    private val repository: PromiseRepository
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

            repository.getPromiseByCodeAndListen(code).collect {
                it.onSuccess {
                    val promiseModel = PromiseMapper.asUiModel(it)
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
}