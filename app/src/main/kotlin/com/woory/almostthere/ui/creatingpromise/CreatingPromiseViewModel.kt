package com.woory.almostthere.ui.creatingpromise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.almostthere.model.LocationModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

class CreatingPromiseViewModel : ViewModel() {

    private val _promiseLocation: MutableSharedFlow<LocationModel?> = MutableSharedFlow()
    val promiseLocation: SharedFlow<LocationModel?> = _promiseLocation.asSharedFlow()

    private val _promiseDate: MutableSharedFlow<LocalDate?> = MutableSharedFlow()
    val promiseDate: SharedFlow<LocalDate?> = _promiseDate.asSharedFlow()

    private val _promiseTime: MutableSharedFlow<LocalTime?> = MutableSharedFlow()
    val promiseTime: SharedFlow<LocalTime?> = _promiseTime.asSharedFlow()

    private val _gameTime: MutableSharedFlow<Duration?> = MutableSharedFlow()
    val gameTime: SharedFlow<Duration?> = _gameTime.asSharedFlow()

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

    fun setPromiseDate(value: LocalDate?) {
        viewModelScope.launch {
            _promiseDate.emit(value)
        }
    }

    fun setPromiseTime(value: LocalTime?) {
        viewModelScope.launch {
            _promiseTime.emit(value)
        }
    }

    fun setGameTime(value: Duration?) {
        viewModelScope.launch {
            _gameTime.emit(value)
        }
    }

    fun createPromise() {
        TODO("약속 생성하여 전달")
    }
}