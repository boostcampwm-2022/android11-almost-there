package com.woory.firebase.mapper

import com.woory.data.model.UserHpModel
import com.woory.firebase.model.UserHp

internal fun UserHp.toUserHpModel() = UserHpModel(
    id = this.Id,
    hp = this.Hp
)