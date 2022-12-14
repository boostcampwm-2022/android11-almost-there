package com.woory.almostthere.presentation.model.user.gameresult

import com.woory.almostthere.presentation.model.UserData

sealed interface UserSplitMoneyItem {

    data class UserSplitMoney(
        val userId: String,
        val userData: UserData,
        val rankingNumber: Int,
        val moneyToPay: Int
    ) : UserSplitMoneyItem

    data class Balance(val value: Int) : UserSplitMoneyItem
}