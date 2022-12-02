package com.woory.presentation.ui.gaming

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skt.tmap.TMapPoint
import com.skt.tmap.overlay.TMapMarkerItem
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.model.AddedUserHp
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.MagneticInfo
import com.woory.presentation.model.UserLocation
import com.woory.presentation.model.UserProfileImage
import com.woory.presentation.model.mapper.location.asDomain
import com.woory.presentation.model.mapper.location.asUiModel
import com.woory.presentation.model.mapper.magnetic.asUiModel
import com.woory.presentation.model.mapper.promise.asUiModel
import com.woory.presentation.model.mapper.user.asUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GamingViewModel @Inject constructor(
    private val repository: PromiseRepository
) : ViewModel() {
    private val _gameCode: MutableStateFlow<String> = MutableStateFlow("")
    val gameCode: StateFlow<String> = _gameCode.asStateFlow()

    private val _userDefaultMarker: MutableStateFlow<Bitmap?> = MutableStateFlow(null)
    private val userDefaultMarker: StateFlow<Bitmap?> = _userDefaultMarker.asStateFlow()

    private val _errorState: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errorState: SharedFlow<Throwable> = _errorState.asSharedFlow()

    private val _magneticInfo: MutableStateFlow<MagneticInfo?> = MutableStateFlow(null)
    val magneticInfo: StateFlow<MagneticInfo?> = _magneticInfo.asStateFlow()

    private val _userLocationEvent: MutableStateFlow<UserLocation?> = MutableStateFlow(null)
    val userLocationEvent: StateFlow<UserLocation?> = _userLocationEvent.asStateFlow()

    private val _allUsers: MutableStateFlow<List<String>?> = MutableStateFlow(null)
    val allUsers: StateFlow<List<String>?> = _allUsers.asStateFlow()

    private val userMarkers: MutableMap<String, TMapMarkerItem> = mutableMapOf()

    val userHpMap: MutableMap<String, MutableStateFlow<AddedUserHp?>> = mutableMapOf()

    private val userImageMap: MutableMap<String, MutableStateFlow<UserProfileImage>> =
        mutableMapOf()

    val userLocationMap: MutableMap<String, MutableStateFlow<UserLocation?>> = mutableMapOf()

    val userNameMap: MutableMap<String, MutableStateFlow<String>> = mutableMapOf()

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
                    launch {
                        repository.getMagneticInfoByCodeAndListen(code).collect { result ->
                            result.onSuccess { magneticInFoModel ->
                                _magneticInfo.emit(magneticInFoModel.asUiModel())
                            }.onFailure { throwable ->
                                _errorState.emit(throwable)
                            }
                        }
                    }

                    // TODO : 실시간 순위 가져오는 코드
                    launch {
                        repository.getGameRealtimeRanking(code).collect{ result ->
                            result.onSuccess {

                            }.onFailure {

                            }
                        }
                    }

                    val uiModel = it.asUiModel()
                    uiModel.data.users.forEach { user ->

                        userHpMap[user.userId] = MutableStateFlow(null)
                        userLocationMap[user.userId] = MutableStateFlow(null)
                        userImageMap[user.userId] = MutableStateFlow(user.data.profileImage)
                        userNameMap[user.userId] = MutableStateFlow(user.data.name)

                        launch {
                            repository.getUserLocation(user.userId).collect { result ->
                                result.onSuccess { userLocationModel ->
                                    val uiLocationModel = userLocationModel.asUiModel()
                                    _userLocationEvent.emit(uiLocationModel)
                                    userLocationMap[user.userId]?.emit(uiLocationModel)
                                }
                            }
                        }

                        // TODO : 유저 hp 정보 받아오기
                        launch {
                            repository.getUserHpAndListen(code, user.userId).collect { result ->
                                result.onSuccess { addedUserHpModel ->
                                    userHpMap[user.userId]?.emit(addedUserHpModel.asUiState())
                                }
                            }
                        }
                    }
                    _allUsers.emit(uiModel.data.users.map { it.userId })
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

    fun getUserImage(id: String): UserProfileImage? = userImageMap[id]?.value

    fun getUserRanking(id: String): Int? {
        return userHpMap[id]?.value?.let {
            userHpMap.values.map { it.value }.indexOf(it) + 1
        }
    }

    suspend fun getAddress(id: String): String? =
        withContext(viewModelScope.coroutineContext) {
            userLocationMap[id]?.value?.let {
                repository.getAddressByPoint(it.geoPoint.asDomain()).getOrNull()
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