package com.woory.presentation.ui.creatingpromise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.presentation.model.Location
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationSearchViewModel : ViewModel() {
    private val _location: MutableStateFlow<Location?> = MutableStateFlow(null)
    val location: StateFlow<Location?> = _location.asStateFlow()

    private val _isMapReady: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isMapReady: StateFlow<Boolean> = _isMapReady.asStateFlow()

    fun setPromiseLocation(value: Location?) {
        viewModelScope.launch {
            _location.emit(value)
        }
    }

    fun setMapReady(value: Boolean) {
        viewModelScope.launch {
            _isMapReady.emit(value)
        }
    }
}