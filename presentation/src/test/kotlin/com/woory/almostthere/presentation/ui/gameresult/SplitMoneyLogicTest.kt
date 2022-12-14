package com.woory.almostthere.presentation.ui.gameresult

import com.woory.almostthere.presentation.model.UserData
import com.woory.almostthere.presentation.model.UserProfileImage
import com.woory.almostthere.presentation.model.user.gameresult.UserRanking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class SplitMoneyLogicTest {

    /**
     * 1 ~ 6순위까지 1명씩 있는 케이스
     * */
    lateinit var normalCase: List<UserRanking>
    /**
     * 공동 1위가 있는 케이스
     * */
    lateinit var tiedBestRankingCase: List<UserRanking>
    /**
     * 그 외 공동 순위가 있는 케이스
     * */
    lateinit var tiedOtherRankingCase: List<UserRanking>

    @Before
    fun setup_dummy_data() {
        normalCase = listOf(
            UserRanking(
                userId = "1",
                userData = UserData("수진", UserProfileImage("#121212", 0)),
                85,
                1
            ), UserRanking(
                userId = "2",
                userData = UserData("재우1", UserProfileImage("#121212", 0)),
                85,
                2
            ),
            UserRanking(
                userId = "3",
                userData = UserData("호현1", UserProfileImage("#121212", 0)),
                85,
                3
            ),
            UserRanking(
                userId = "4",
                userData = UserData("재우2", UserProfileImage("#ff0000", 1)),
                50,
                4
            ), UserRanking(
                userId = "5",
                userData = UserData("호현2", UserProfileImage("#ff00ff", 2)),
                20,
                5
            ), UserRanking(
                userId = "6",
                userData = UserData("도명", UserProfileImage("#00ffff", 3)),
                10,
                6
            )
        )

        tiedBestRankingCase = listOf(
            UserRanking(
                userId = "1",
                userData = UserData("수진", UserProfileImage("#121212", 0)),
                85,
                1
            ), UserRanking(
                userId = "1",
                userData = UserData("재우1", UserProfileImage("#121212", 0)),
                85,
                1
            ),
            UserRanking(
                userId = "3",
                userData = UserData("호현1", UserProfileImage("#121212", 0)),
                85,
                3
            ),
            UserRanking(
                userId = "4",
                userData = UserData("재우2", UserProfileImage("#ff0000", 1)),
                50,
                4
            ), UserRanking(
                userId = "5",
                userData = UserData("호현2", UserProfileImage("#ff00ff", 2)),
                20,
                5
            ), UserRanking(
                userId = "6",
                userData = UserData("도명", UserProfileImage("#00ffff", 3)),
                10,
                6
            )
        )

        tiedOtherRankingCase = listOf(
            UserRanking(
                userId = "1",
                userData = UserData("수진", UserProfileImage("#121212", 0)),
                85,
                1
            ), UserRanking(
                userId = "2",
                userData = UserData("재우1", UserProfileImage("#121212", 0)),
                85,
                1
            ),
            UserRanking(
                userId = "3",
                userData = UserData("호현1", UserProfileImage("#121212", 0)),
                85,
                3
            ),
            UserRanking(
                userId = "5",
                userData = UserData("재우2", UserProfileImage("#ff0000", 1)),
                50,
                5
            ), UserRanking(
                userId = "5",
                userData = UserData("호현2", UserProfileImage("#ff00ff", 2)),
                20,
                5
            ), UserRanking(
                userId = "5",
                userData = UserData("도명", UserProfileImage("#00ffff", 3)),
                10,
                5
            )
        )
    }

    @Test
    fun calculatePayment_normalCase() {
        val totalPayment = 60000
        val result = SplitMoneyLogic.calculatePayment(totalPayment, normalCase)
        Assert.assertEquals(true, result.sumOf { it.moneyToPay } <= totalPayment)
        Assert.assertEquals(2857, result.minByOrNull { it.moneyToPay }?.moneyToPay)
    }

    @Test
    fun calculatePayment_tiedBestRankingCase() {
        val totalPayment = 60000
        val result = SplitMoneyLogic.calculatePayment(totalPayment, tiedBestRankingCase)
        Assert.assertEquals(true, result.sumOf { it.moneyToPay } <= totalPayment)
        Assert.assertEquals(3000, result.minByOrNull { it.moneyToPay }?.moneyToPay)
    }

    @Test
    fun calculatePayment_tiedOtherRankingCase() {
        val totalPayment = 60000
        val result = SplitMoneyLogic.calculatePayment(totalPayment, tiedOtherRankingCase)
        Assert.assertEquals(true, result.sumOf { it.moneyToPay } <= totalPayment)
        Assert.assertEquals(3000, result.minByOrNull { it.moneyToPay }?.moneyToPay)
    }

    @Test
    fun calculatePayment_max() {
        val totalPayment = 5000000
        val result = SplitMoneyLogic.calculatePayment(totalPayment, tiedOtherRankingCase)
        Assert.assertEquals(true, result.sumOf { it.moneyToPay } <= totalPayment)
    }

    @Test
    fun calculatePayment_min() {
        val totalPayment = 1
        val result = SplitMoneyLogic.calculatePayment(totalPayment, tiedOtherRankingCase)
        Assert.assertEquals(true, result.sumOf { it.moneyToPay } <= totalPayment)
        Assert.assertEquals(0, result.minByOrNull { it.moneyToPay }?.moneyToPay)
        Assert.assertEquals(0, result.maxByOrNull { it.moneyToPay }?.moneyToPay)
    }
}