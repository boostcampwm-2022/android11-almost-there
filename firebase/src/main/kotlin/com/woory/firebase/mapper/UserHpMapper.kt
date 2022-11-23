package com.woory.firebase.mapper

import com.woory.data.model.UserHpModel
import com.woory.firebase.model.UserHpDocument

internal fun UserHpDocument.toUserHpModel() = UserHpModel(
    id = this.id,
    hp = this.hp
)

internal fun UserHpModel.toUserHp() = UserHpDocument(
    id = this.id,
    hp = this.hp
)