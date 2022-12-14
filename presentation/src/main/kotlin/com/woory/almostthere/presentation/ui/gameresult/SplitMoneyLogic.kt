package com.woory.almostthere.presentation.ui.gameresult

import com.woory.almostthere.presentation.model.user.gameresult.UserRanking
import com.woory.almostthere.presentation.model.user.gameresult.UserSplitMoneyItem

object SplitMoneyLogic {

    private fun getRankingPayment(
        rankings: List<UserRanking>,
        totalPayment: Int
    ): Map<Int, Int?> {
        val total = rankings.sumOf { it.rankingNumber }

        return (1..rankings.count()).associateWith { rankingNumber ->
            val rankingNumberCount = rankings.count { it.rankingNumber == rankingNumber }
            if (rankingNumberCount == 0) null
            else (totalPayment * (rankingNumber.toDouble() / total.toDouble())).toInt()
        }.filterNot { it.value == null }
    }

    fun calculatePayment(totalPayment: Int, _rankings: List<UserRanking>): List<UserSplitMoneyItem.UserSplitMoney> {
        val rankings = _rankings.sortedBy { it.rankingNumber }
        val rankingPayment = getRankingPayment(rankings, totalPayment)
        return rankings.map {
            UserSplitMoneyItem.UserSplitMoney(
                userId = it.userId,
                userData = it.userData,
                rankingNumber = it.rankingNumber,
                moneyToPay = rankingPayment[it.rankingNumber] ?: 0
            )
        }
    }
}