package com.woory.almostthere.presentation.model.mapper.user

import com.woory.almostthere.data.model.UserRankingModel
import com.woory.almostthere.presentation.model.mapper.UiModelMapper
import com.woory.almostthere.presentation.model.user.gameresult.UserRanking

object UserRankingMapper : UiModelMapper<UserRanking, UserRankingModel> {

    override fun asUiModel(domain: UserRankingModel): UserRanking = UserRanking(
        userId = domain.userId,
        userData = domain.userData.asUiModel(),
        hp = domain.hp,
        rankingNumber = domain.rankingNumber
    )

    override fun asDomain(uiModel: UserRanking): UserRankingModel =
        UserRankingModel(
            userId = uiModel.userId,
            userData = uiModel.userData.asDomain(),
            hp = uiModel.hp,
            rankingNumber = uiModel.rankingNumber
        )
}

fun UserRanking.asDomain(): UserRankingModel = UserRankingMapper.asDomain(this)

fun UserRankingModel.asUiModel(): UserRanking = UserRankingMapper.asUiModel(this)