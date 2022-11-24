package com.woory.presentation.ui.promiseinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.model.PromiseModel
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.model.Promise
import com.woory.presentation.model.mapper.promise.PromiseMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromiseInfoViewModel @Inject constructor(
    private val repository: PromiseRepository
) : ViewModel() {
    private val _gameCode: MutableStateFlow<String> = MutableStateFlow("")
    private val gameCode: StateFlow<String> = _gameCode.asStateFlow()

    private val _isMapReady: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isMapReady: StateFlow<Boolean> = _isMapReady.asStateFlow()

    private val _uiState: MutableStateFlow<PromiseUiState> =
        MutableStateFlow(PromiseUiState.Waiting)
    val uiState: StateFlow<PromiseUiState> = _uiState.asStateFlow()

    private val _errorState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val errorState: StateFlow<Boolean> = _errorState.asStateFlow()

    private val _promiseModel: MutableStateFlow<Promise?> = MutableStateFlow(null)
    val promiseModel: StateFlow<Promise?> = _promiseModel.asStateFlow()

    fun setGameCode(code: String) {
        viewModelScope.launch {
            _gameCode.emit(code)
        }
    }

    fun setMapReady(isMapReady: Boolean) {
        viewModelScope.launch {
            _isMapReady.emit(isMapReady)
        }
    }

    fun setUiState(state: PromiseUiState) {
        viewModelScope.launch {
            _uiState.emit(state)
        }
    }

    fun setErrorState(boolean: Boolean){
        viewModelScope.launch {
            _errorState.emit(boolean)
        }
    }

    fun fetchPromiseDate() {
        viewModelScope.launch {
            val code = gameCode.value
            _uiState.emit(PromiseUiState.Loading)
            repository.getPromiseByCode(code).onSuccess {
                _promiseModel.emit(PromiseMapper.asUiModel(it))
                _uiState.emit(PromiseUiState.Success)
            }.onFailure {
                setErrorState(true)
            }
        }
    }
}