package com.woory.presentation.ui.gameresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woory.presentation.model.UserData
import com.woory.presentation.model.UserPayment
import com.woory.presentation.model.UserProfileImage
import com.woory.presentation.model.UserRanking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameResultViewModel @Inject constructor() : ViewModel() {

    private val _userRankingList: MutableStateFlow<List<UserRanking>?> =
        MutableStateFlow(null)
    val userRankingList: StateFlow<List<UserRanking>?> = _userRankingList.asStateFlow()

    private val _myPayment: MutableStateFlow<Int> = MutableStateFlow(0)
    val myPayment: StateFlow<Int> = _myPayment.asStateFlow()

    private val _userPaymentList: MutableStateFlow<List<UserPayment>?> =
        MutableStateFlow(null)
    val userPaymentAdapter: StateFlow<List<UserPayment>?> = _userPaymentList.asStateFlow()

    init {
        fetchUserRankingList()
        loadUserPaymentList()
    }

    private fun fetchUserRankingList() {
        viewModelScope.launch {
            // TODO("테스트값")
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
            _userRankingList.emit(testUserRankingList)
        }
    }

    private fun loadUserPaymentList() {
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