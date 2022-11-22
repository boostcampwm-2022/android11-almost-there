package com.woory.firebase.mapper

import com.woory.data.model.UserHpModel
import com.woory.firebase.model.UserHp

internal fun UserHp.toUserHpModel() = UserHpModel(
    id = this.id,
    hp = this.hp
)

internal fun UserHpModel.toUserHp() = UserHp(
    id = this.id,
    hp = this.hp
)