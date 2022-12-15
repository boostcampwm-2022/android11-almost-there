package com.woory.almostthere.presentation.model.mapper.user

import com.woory.almostthere.data.model.UserHpModel
import com.woory.almostthere.presentation.model.UserHp
import com.woory.almostthere.presentation.model.mapper.UiModelMapper

object AddedUserHpMapper : UiModelMapper<UserHp, UserHpModel> {
    override fun asUiModel(domain: UserHpModel): UserHp =
        UserHp(
            userId = domain.userId,
            hp = domain.hp,
            arrived = domain.arrived,
            lost = domain.lost,
            updatedAt = domain.updatedAt
        )

    override fun asDomain(uiModel: UserHp): UserHpModel =
        UserHpModel(
            userId = uiModel.userId,
            hp = uiModel.hp,
            arrived = uiModel.arrived,
            lost = uiModel.lost,
            updatedAt = uiModel.updatedAt
        )
}

internal fun UserHpModel.asUiState() = AddedUserHpMapper.asUiModel(this)

internal fun UserHp.asDomain() = AddedUserHpMapper.asDomain(this)