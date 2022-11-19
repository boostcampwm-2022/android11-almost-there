package com.woory.almostthere.ui.creatingpromise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.almostthere.model.DateModel
import com.woory.almostthere.model.LocationModel
import com.woory.almostthere.model.TimeModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CreatingPromiseViewModel : ViewModel() {

    private val _promiseLocation: MutableSharedFlow<LocationModel?> = MutableSharedFlow()
    val promiseLocation: SharedFlow<LocationModel?> = _promiseLocation.asSharedFlow()

    private val _promiseDate: MutableSharedFlow<DateModel?> = MutableSharedFlow()
    val promiseDate: SharedFlow<DateModel?> = _promiseDate.asSharedFlow()

    private val _promiseTime: MutableSharedFlow<TimeModel?> = MutableSharedFlow()
    val promiseTime: SharedFlow<TimeModel?> = _promiseTime.asSharedFlow()

    private val _gameTime: MutableSharedFlow<TimeModel?> = MutableSharedFlow()
    val gameTime: SharedFlow<TimeModel?> = _gameTime.asSharedFlow()

    val isEnabled: Flow<Boolean> = combine(
        _promiseLocation,
        _promiseDate,
        _promiseTime,
        _gameTime
    ) { _promiseLocation, _promiseDate, _promiseTime, _gameTime ->
        (_promiseLocation != null) && (_promiseDate != null) && (_promiseTime != null) && (_gameTime != null)
    }

    fun setPromiseLocation(value: LocationModel?) {
        viewModelScope.launch {
            _promiseLocation.emit(value)
        }
    }

    fun setPromiseDate(value: DateModel?) {
        viewModelScope.launch {
            _promiseDate.emit(value)
        }
    }

    fun setPromiseTime(value: TimeModel?) {
        viewModelScope.launch {
            _promiseTime.emit(value)
        }
    }

    fun setGameTime(value: TimeModel?) {
        viewModelScope.launch {
            _gameTime.emit(value)
        }
    }

    fun createPromise() {
        TODO("약속 생성하여 전달")
    }
}