package com.woory.presentation.ui.creatingpromise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.presentation.model.LocationModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationSearchViewModel : ViewModel() {
    private val _location: MutableStateFlow<LocationModel?> = MutableStateFlow(null)
    val location: StateFlow<LocationModel?> = _location.asStateFlow()

    private val _isMapReady: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isMapReady: StateFlow<Boolean> = _isMapReady.asStateFlow()

    fun setPromiseLocation(value: LocationModel?) {
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