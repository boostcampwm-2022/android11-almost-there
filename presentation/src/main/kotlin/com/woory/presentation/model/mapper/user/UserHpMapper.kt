package com.woory.presentation.model.mapper.user

import com.woory.data.model.UserHpModel
import com.woory.presentation.model.UserHp
import com.woory.presentation.model.mapper.UiModelMapper

object UserHpMapper : UiModelMapper<UserHp, UserHpModel> {
    override fun asUiModel(domain: UserHpModel): UserHp =
        UserHp(domain.id, domain.hp)

    override fun asDomain(uiModel: UserHp): UserHpModel =
        UserHpModel(uiModel.userId, uiModel.hp)
}

internal fun UserHp.asDomain(): UserHpModel = UserHpMapper.asDomain(this)

internal fun UserHpModel.asUiModel(): UserHp = UserHpMapper.asUiModel(this)