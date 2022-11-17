package com.woory.almostthere.ui.creatingpromise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.almostthere.model.DateUiState
import com.woory.almostthere.model.LocationUiState
import com.woory.almostthere.model.TimeUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CreatingPromiseViewModel : ViewModel() {

    private val _promiseLocation: MutableSharedFlow<LocationUiState?> = MutableSharedFlow()
    val promiseLocation: SharedFlow<LocationUiState?> = _promiseLocation.asSharedFlow()

    private val _promiseDate: MutableSharedFlow<DateUiState?> = MutableSharedFlow()
    val promiseDate: SharedFlow<DateUiState?> = _promiseDate.asSharedFlow()

    private val _promiseTime: MutableSharedFlow<TimeUiState?> = MutableSharedFlow()
    val promiseTime: SharedFlow<TimeUiState?> = _promiseTime.asSharedFlow()

    private val _gameTime: MutableSharedFlow<TimeUiState?> = MutableSharedFlow()
    val gameTime: SharedFlow<TimeUiState?> = _gameTime.asSharedFlow()

    val isEnabled: Flow<Boolean> = combine(
        _promiseLocation,
        _promiseDate,
        _promiseTime,
        _gameTime
    ) { _promiseLocation, _promiseDate, _promiseTime, _gameTime ->
        (_promiseLocation != null) && (_promiseDate != null) && (_promiseTime != null) && (_gameTime != null)
    }

    fun setPromiseLocation(value: LocationUiState?) {
        viewModelScope.launch {
            _promiseLocation.emit(value)
        }
    }

    fun setPromiseDate(value: DateUiState?) {
        viewModelScope.launch {
            _promiseDate.emit(value)
        }
    }

    fun setPromiseTime(value: TimeUiState?) {
        viewModelScope.launch {
            _promiseTime.emit(value)
        }
    }

    fun setGameTime(value: TimeUiState?) {
        viewModelScope.launch {
            _gameTime.emit(value)
        }
    }

    fun createPromise() {
        TODO("약속 생성하여 전달")
    }
}