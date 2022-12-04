package com.woory.presentation.model.mapper.user

import com.woory.data.model.AddedUserHpModel
import com.woory.presentation.model.AddedUserHp
import com.woory.presentation.model.mapper.UiModelMapper

object AddedUserHpMapper : UiModelMapper<AddedUserHp, AddedUserHpModel> {
    override fun asUiModel(domain: AddedUserHpModel): AddedUserHp =
        AddedUserHp(
            userId = domain.userId,
            hp = domain.hp,
            arrived = domain.arrived,
            lost = domain.lost,
            updatedAt = domain.updatedAt
        )

    override fun asDomain(uiModel: AddedUserHp): AddedUserHpModel =
        AddedUserHpModel(
            userId = uiModel.userId,
            hp = uiModel.hp,
            arrived = uiModel.arrived,
            lost = uiModel.lost,
            updatedAt = uiModel.updatedAt
        )
}

internal fun AddedUserHpModel.asUiState() = AddedUserHpMapper.asUiModel(this)

internal fun AddedUserHp.asDomain() = AddedUserHpMapper.asDomain(this)