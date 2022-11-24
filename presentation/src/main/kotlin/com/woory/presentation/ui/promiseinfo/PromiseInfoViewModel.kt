package com.woory.presentation.ui.promiseinfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.model.Promise
import com.woory.presentation.model.User
import com.woory.presentation.model.UserData
import com.woory.presentation.model.UserProfileImage
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
    val gameCode: StateFlow<String> = _gameCode.asStateFlow()

    private val _isMapReady: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isMapReady: StateFlow<Boolean> = _isMapReady.asStateFlow()

    private val _uiState: MutableStateFlow<PromiseUiState> =
        MutableStateFlow(PromiseUiState.Waiting)
    val uiState: StateFlow<PromiseUiState> = _uiState.asStateFlow()

    private val _errorState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val errorState: StateFlow<Boolean> = _errorState.asStateFlow()

    private val _promiseModel: MutableStateFlow<Promise?> = MutableStateFlow(null)
    val promiseModel: StateFlow<Promise?> = _promiseModel.asStateFlow()

    private val _host: MutableStateFlow<String> = MutableStateFlow("")
    private val host: StateFlow<String> = _host.asStateFlow()

    val dummyUsers = listOf(
        User("testCode", UserData("조재우", UserProfileImage("#FF0000", 0))),
        User("testCode2", UserData("전도명", UserProfileImage("#00FF00", 1))),
        User("testCode3", UserData("이수진", UserProfileImage("#0000FF", 2))),
        User("testCode4", UserData("유호현", UserProfileImage("#FF00FF", 3)))
    )

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

    fun setErrorState(boolean: Boolean) {
        viewModelScope.launch {
            _errorState.emit(boolean)
        }
    }

    fun isHost(userId: String): Boolean {
        return host.value == userId
    }

    fun fetchPromiseDate() {
        viewModelScope.launch {
            val code = gameCode.value
            _uiState.emit(PromiseUiState.Loading)
            repository.getPromiseByCode(code).onSuccess {
                val promiseModel = PromiseMapper.asUiModel(it)
                _promiseModel.emit(promiseModel)
                _uiState.emit(PromiseUiState.Success)
                _host.emit(promiseModel.data.host.userId)
            }.onFailure {
                setErrorState(true)
            }
        }
    }
}