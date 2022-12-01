package com.woory.presentation.ui.gameresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.data.repository.PromiseRepository
import com.woory.data.repository.UserRepository
import com.woory.presentation.model.UserData
import com.woory.presentation.model.UserPayment
import com.woory.presentation.model.UserProfileImage
import com.woory.presentation.model.UserRanking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameResultViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val promiseRepository: PromiseRepository
) : ViewModel() {

    private val _gameCode: MutableStateFlow<String?> = MutableStateFlow(null)
    val gameCode: StateFlow<String?> = _gameCode.asStateFlow()

    private val _userRankingList: MutableStateFlow<List<UserRanking>?> = MutableStateFlow(null)

    val userRankingList: StateFlow<List<UserRanking>?> = _userRankingList.asStateFlow()

    private val _myPayment: MutableStateFlow<Int?> = MutableStateFlow(null)
    val myPayment: StateFlow<Int?> = _myPayment.asStateFlow()

    private val _userPaymentList: MutableStateFlow<List<UserPayment>?> =
        MutableStateFlow(null)
    val userPaymentList: StateFlow<List<UserPayment>?> = _userPaymentList.asStateFlow()

    private val _dataLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val dataLoading: StateFlow<Boolean> = _dataLoading.asStateFlow()

    private val _errorEvent: MutableSharedFlow<Throwable> = MutableSharedFlow()
    val errorEvent: SharedFlow<Throwable> = _errorEvent.asSharedFlow()

    fun setGameCode(gameCode: String?) {
        viewModelScope.launch {
            _gameCode.emit(gameCode)
        }
    }

    fun fetchUserRankingList() {
        viewModelScope.launch {

            if (_gameCode.value == null) {
                _errorEvent.emit(IllegalArgumentException("참여 코드가 없습니다."))
                return@launch
            }

            _dataLoading.emit(true)

//            // TODO("Repository 값 가져와서 처리")
//            promiseRepository.getGameResultByCode().onSuccess {
//
//            }.onFailture {
//
//            }

            val testUserRankingList = listOf(
                UserRanking(
                    userId = "1",
                    userData = UserData("수진", UserProfileImage("#121212", 0)),
                    85,
                    1
                ), UserRanking(
                    userId = "2",
                    userData = UserData("재우", UserProfileImage("#ff0000", 1)),
                    50,
                    1
                ), UserRanking(
                    userId = "3",
                    userData = UserData("호현", UserProfileImage("#ff00ff", 2)),
                    20,
                    1
                ), UserRanking(
                    userId = "4",
                    userData = UserData("도명", UserProfileImage("#00ffff", 3)),
                    10,
                    1
                )
            )
            _dataLoading.emit(false)
            _userRankingList.emit(testUserRankingList)
        }
    }

    fun loadUserPaymentList(value: Int) {
        viewModelScope.launch {
            // TODO("테스트값")
            val testUserPaymentList = listOf(
                UserPayment(
                    userId = "1",
                    userData = UserData("수진", UserProfileImage("#121212", 0)),
                    1,
                    10000
                ), UserPayment(
                    userId = "2",
                    userData = UserData("재우", UserProfileImage("#ff0000", 1)),
                    2,
                    20000
                ), UserPayment(
                    userId = "3",
                    userData = UserData("호현", UserProfileImage("#ff00ff", 2)),
                    3,
                    30000
                ), UserPayment(
                    userId = "4",
                    userData = UserData("도명", UserProfileImage("#00ffff", 3)),
                    4,
                    40000
                )
            )
            _userPaymentList.emit(testUserPaymentList)
            _myPayment.emit(testUserPaymentList[0].payment)
        }
    }
}