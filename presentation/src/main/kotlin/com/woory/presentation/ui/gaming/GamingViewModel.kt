package com.woory.presentation.ui.gaming

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skt.tmap.TMapPoint
import com.skt.tmap.overlay.TMapMarkerItem
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.model.MagneticInfo
import com.woory.presentation.model.Promise
import com.woory.presentation.model.UserLocation
import com.woory.presentation.model.mapper.location.asUiModel
import com.woory.presentation.model.mapper.magnetic.asUiModel
import com.woory.presentation.model.mapper.promise.asUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GamingViewModel @Inject constructor(
    private val repository: PromiseRepository
) : ViewModel() {
    private val _gameCode: MutableStateFlow<String> = MutableStateFlow("")
    private val gameCode: StateFlow<String> = _gameCode.asStateFlow()

    private val _userDefaultMarker: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    private val userDefaultMarker: StateFlow<Bitmap?> = _userDefaultMarker.asStateFlow()

    private val _promiseModel: MutableStateFlow<Promise?> = MutableStateFlow(null)
    val promiseModel: StateFlow<Promise?> = _promiseModel.asStateFlow()

    private val _errorState: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errorState: SharedFlow<Throwable> = _errorState.asSharedFlow()

    private val _magneticInfo: MutableStateFlow<MagneticInfo?> = MutableStateFlow(null)
    val magneticInfo: StateFlow<MagneticInfo?> = _magneticInfo.asStateFlow()

    private val _userLocation: MutableStateFlow<UserLocation?> = MutableStateFlow(null)
    val userLocation: StateFlow<UserLocation?> = _userLocation.asStateFlow()

    private val userMarkers: MutableMap<String, TMapMarkerItem> = mutableMapOf()

    fun setGameCode(code: String) {
        viewModelScope.launch {
            _gameCode.emit(code)
        }
    }

    fun fetchPromiseData() {
        viewModelScope.launch {
            val code = gameCode.value
            repository
                .getPromiseByCode(code)
                .onSuccess {
                    val uiModel = it.asUiModel()

                    repository.getMagneticInfoByCode(code).onSuccess { magneticInfoModel ->
                        _magneticInfo.emit(magneticInfoModel.asUiModel())
                    }.onFailure { throwable ->
                        _errorState.emit(throwable)
                    }

                    uiModel.data.users.forEach { user ->
                        launch {
                            repository.getUserLocation(user.userId).collect { result ->
                                result.onSuccess { userLocationModel ->
                                    _userLocation.emit(userLocationModel.asUiModel())
                                }.onFailure { throwable ->
                                    _errorState.emit(throwable)
                                }
                            }
                        }
                    }
                    _promiseModel.emit(uiModel)
                }.onFailure {
                    _errorState.emit(it)
                }
        }
    }

    fun setDefaultMarker(marker: Bitmap) {
        viewModelScope.launch {
            _userDefaultMarker.emit(marker)
        }
    }

    fun setUserMarker(newData: UserLocation) {
        if (userMarkers[newData.token] == null) {
            userMarkers[newData.token] = TMapMarkerItem().apply {
                id = newData.token
                icon = userDefaultMarker.value
            }
        }

        requireNotNull(userMarkers[newData.token]).tMapPoint = TMapPoint(
            newData.geoPoint.latitude, newData.geoPoint.longitude
        )
    }

    fun getUserMarker(token: String): TMapMarkerItem = requireNotNull(userMarkers[token])
}