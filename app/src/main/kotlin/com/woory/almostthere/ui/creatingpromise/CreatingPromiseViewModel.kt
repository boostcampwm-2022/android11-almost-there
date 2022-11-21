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

    private val _promiseLocation: MutableStateFlow<LocationModel?> =  MutableStateFlow(null)
    val promiseLocation: StateFlow<LocationModel?> = _promiseLocation.asStateFlow()

    private val _promiseDate: MutableStateFlow<LocalDate?> =  MutableStateFlow(null)
    val promiseDate: StateFlow<LocalDate?> = _promiseDate.asStateFlow()

    private val _promiseTime: MutableStateFlow<LocalTime?> = MutableStateFlow(null)
    val promiseTime: StateFlow<LocalTime?> = _promiseTime.asStateFlow()

    private val _gameTime: MutableStateFlow<Duration?> = MutableStateFlow(null)
    val gameTime: StateFlow<Duration?> = _gameTime.asStateFlow()

    val isEnabled: Flow<Boolean> = combine(
        promiseLocation,
        promiseDate,
        promiseTime,
        gameTime
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