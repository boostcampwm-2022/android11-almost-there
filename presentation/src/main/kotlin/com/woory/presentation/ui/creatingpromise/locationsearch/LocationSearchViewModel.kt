package com.woory.presentation.ui.creatingpromise.locationsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.Location
import com.woory.presentation.model.mapper.location.asDomain
import com.woory.presentation.ui.creatingpromise.CreatingPromiseUiState
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
class LocationSearchViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<CreatingPromiseUiState> =
        MutableStateFlow(CreatingPromiseUiState.Success)
    val uiState: StateFlow<CreatingPromiseUiState> = _uiState.asStateFlow()

    private val _errorEvent: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errorEvent: SharedFlow<Throwable> = _errorEvent.asSharedFlow()

    private val _promiseLocation: MutableStateFlow<Location?> = MutableStateFlow(null)
    val promiseLocation: StateFlow<Location?> = _promiseLocation.asStateFlow()

    private val _isSearchMapReady: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSearchMapReady: StateFlow<Boolean> = _isSearchMapReady.asStateFlow()

    fun findAddressByLocation(geoPoint: GeoPoint?) {
        viewModelScope.launch {
            setStateLoading()
            geoPoint?.let {
                promiseRepository.getAddressByPoint(geoPoint.asDomain()).onSuccess { address ->
                    setStateSuccess()
                    _promiseLocation.emit(Location(geoPoint, address))
                }.onFailure { throwable ->
                    setStateError(throwable)
                }
            } ?: setStateError(java.util.NoSuchElementException("검색할 위치가 없습니다."))
        }
    }

    fun setIsMapReady(isMapReady: Boolean) {
        viewModelScope.launch {
            _isSearchMapReady.emit(isMapReady)
        }
    }

    private fun setStateLoading() {
        viewModelScope.launch {
            _uiState.emit(CreatingPromiseUiState.Loading)
        }
    }

    private fun setStateSuccess() {
        viewModelScope.launch {
            _uiState.emit(CreatingPromiseUiState.Success)
        }
    }

    private fun setStateError(throwable: Throwable) {
        viewModelScope.launch {
            _uiState.emit(CreatingPromiseUiState.Success)
            _errorEvent.emit(throwable)
        }
    }
}