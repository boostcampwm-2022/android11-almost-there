package com.woory.presentation.ui.gaming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.data.repository.RouteRepository
import com.woory.data.repository.UserRepository
import com.woory.presentation.model.AddedUserHp
import com.woory.presentation.model.MagneticInfo
import com.woory.presentation.model.Promise
import com.woory.presentation.model.User
import com.woory.presentation.model.UserLocation
import com.woory.presentation.model.UserProfileImage
import com.woory.presentation.model.mapper.location.asDomain
import com.woory.presentation.model.mapper.location.asUiModel
import com.woory.presentation.model.mapper.magnetic.asUiModel
import com.woory.presentation.model.mapper.promise.asUiModel
import com.woory.presentation.model.mapper.user.asUiModel
import com.woory.presentation.model.mapper.user.asUiState
import com.woory.presentation.model.user.gameresult.UserRanking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GamingViewModel @Inject constructor(
    private val promiseRepository: PromiseRepository,
    private val userRepository: UserRepository,
    private val routeRepository: RouteRepository
) : ViewModel() {
    private val _gameCode: MutableStateFlow<String> = MutableStateFlow("")
    val gameCode: StateFlow<String> = _gameCode.asStateFlow()

    private val _errorState: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errorState: SharedFlow<Throwable> = _errorState.asSharedFlow()

    private val _magneticInfo: MutableStateFlow<MagneticInfo?> = MutableStateFlow(null)
    val magneticInfo: StateFlow<MagneticInfo?> = _magneticInfo.asStateFlow()

    private val _allUsers: MutableStateFlow<List<User>?> = MutableStateFlow(null)
    val allUsers: StateFlow<List<User>?> = _allUsers.asStateFlow()

    private val _isFinished: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    val userHpMap: MutableMap<String, MutableStateFlow<AddedUserHp?>> = mutableMapOf()

    private val userImageMap: MutableMap<String, MutableStateFlow<UserProfileImage>> =
        mutableMapOf()

    val userLocationMap: MutableMap<String, MutableStateFlow<UserLocation?>> = mutableMapOf()

    val userNameMap: MutableMap<String, MutableStateFlow<String>> = mutableMapOf()

    val myUserInfo = runBlocking { userRepository.userPreferences.first() }

    private val _isArrived: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isArrived: StateFlow<Boolean> = _isArrived.asStateFlow()

    private val _centerLocationToMe: MutableSharedFlow<Unit> = MutableSharedFlow()
    val centerLocationToMe: SharedFlow<Unit> = _centerLocationToMe.asSharedFlow()

    private val _userId: MutableStateFlow<String?> = MutableStateFlow(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    private val _ranking: MutableStateFlow<List<UserRanking>> = MutableStateFlow(listOf())
    val ranking: StateFlow<List<UserRanking>> = _ranking.asStateFlow()

    private val _promiseModel: MutableStateFlow<Promise?> = MutableStateFlow(null)
    val promiseModel: StateFlow<Promise?> = _promiseModel.asStateFlow()

    fun setGameCode(code: String) {
        viewModelScope.launch {
            _gameCode.emit(code)
        }
    }

    fun setUserId() {
        viewModelScope.launch {
            userRepository.userPreferences.collectLatest {
                _userId.emit(it.userID)
            }
        }
    }

    fun fetchPromise() {
        viewModelScope.launch {
            val code = gameCode.value
            promiseRepository.getPromiseByCode(code).onSuccess {
                _promiseModel.emit(it.asUiModel())

                it.data.users.forEach { user ->
                    userHpMap[user.userId] = MutableStateFlow(null)
                    userLocationMap[user.userId] = MutableStateFlow(null)
                    userImageMap[user.userId] = MutableStateFlow(user.data.profileImage.asUiModel())
                    userNameMap[user.userId] = MutableStateFlow(user.data.name)
                }
            }.onFailure {
                _errorState.emit(it)
            }
        }
    }

    fun fetchMagneticField(promise: Promise) {
        viewModelScope.launch {

            _allUsers.emit(promise.data.users)

            launch {
                promiseRepository.getMagneticInfoByCodeAndListen(promise.code)
                    .collectLatest { result ->
                        result.onSuccess { magneticInFoModel ->
                            _magneticInfo.emit(magneticInFoModel.asUiModel())
                        }.onFailure { throwable ->
                            _errorState.emit(throwable)
                        }
                    }
            }
        }
    }

    suspend fun fetchUserLocation(user: User) {
        viewModelScope.launch {
            promiseRepository.getUserLocation(user.userId).collectLatest { result ->
                result.onSuccess { userLocationModel ->
                    val uiLocationModel = userLocationModel.asUiModel()

                    requireNotNull(userLocationMap[user.userId]).emit(uiLocationModel)
                }.onFailure {
                    Timber.tag("123123").d(it)
                }
            }
        }
    }

    // TODO : 실시간 순위 받아오기
    fun fetchRealtimeRanking() {
        viewModelScope.launch {
            val code = gameCode.value
            launch {
                promiseRepository.getGameRealtimeRanking(code).collectLatest { result ->
                    result.onSuccess { list ->
//                        _ranking.emit(list.filter {
//                            !it.lost && !it.arrived
//                        }
//                            .map { addedUserHpModel ->
//                                val id = addedUserHpModel.userId
//                                UserRanking(
//                                    userId = id,
//                                    rank = list.indexOf(addedUserHpModel) + 1,
//                                    profileImage = getUserImage(id) ?: requireNotNull(
//                                        userDefaultImage.value
//                                    ),
//                                    userName = userNameMap[id]?.value ?: "",
//                                    hp = addedUserHpModel.hp
//                                )
//                            })
                    }
                }
            }
        }
    }

    fun fetchGameRanking() {
        viewModelScope.launch {
            val code = gameCode.value
            promiseRepository.getUserRankings(code).onSuccess { rankingList ->
                _ranking.emit(rankingList.map { it.asUiModel() })
            }
        }
    }

    fun fetchUserHp(userId: String) {
        viewModelScope.launch {
            val code = gameCode.value
            launch {
                promiseRepository.getUserHpAndListen(code, userId)
                    .collectLatest { result ->
                        result.onSuccess { addedUserHpModel ->
                            userHpMap[userId]?.emit(addedUserHpModel.asUiState())
                        }
                    }
            }
        }
    }

    fun fetchUserArrival() {
        viewModelScope.launch {
            val code = gameCode.value
            launch {
                promiseRepository.getPlayerArrived(code, myUserInfo.userID)
                    .collectLatest { result ->
                        result.onSuccess { isArrived ->
                            _isArrived.emit(isArrived)
                        }
                    }
            }
        }
    }

    fun fetchPromiseEnding() {
        viewModelScope.launch {
            val code = gameCode.value
            launch {
                promiseRepository.getIsFinishedPromise(code).collectLatest { result ->
                    result.onSuccess { isFinished ->
                        _isFinished.emit(isFinished)
                    }
                }
            }
        }
    }

    fun getUserImage(id: String): UserProfileImage? = userImageMap[id]?.value

    fun getUserRanking(id: String): Int? {

        return ranking.value.find {
            it.userId == id
        }?.rankingNumber

//        return userHpMap[id]?.value?.let {
//            userHpMap.values.map { it.value }.indexOf(it) + 1
//        }
    }

    suspend fun getAddress(id: String): String? =
        withContext(viewModelScope.coroutineContext) {
            userLocationMap[id]?.value?.let {
                promiseRepository.getAddressByPoint(it.geoPoint.asDomain()).getOrNull()
            }
        }

    fun getUserLocation(token: String): UserLocation? = userLocationMap[token]?.value

    suspend fun setUserArrived(gameCode: String, token: String) =
        promiseRepository.setPlayerArrived(gameCode, token)

    fun getMyLocation() {
        viewModelScope.launch {
            _centerLocationToMe.emit(Unit)
        }
    }

    suspend fun getRemainTime(): Int? =
        withContext(viewModelScope.coroutineContext) {
            userId.value?.let { id ->
                getUserLocation(id)?.geoPoint?.let { myPoint ->
                    magneticInfo.value?.centerPoint?.let { centerPoint ->
                        routeRepository.getMinimumTime(
                            myPoint.asDomain(),
                            centerPoint.asDomain()
                        )
                            .getOrDefault(-1)
                    }
                }
            }

        }
}