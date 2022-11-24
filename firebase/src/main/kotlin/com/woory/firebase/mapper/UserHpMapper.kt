package com.woory.firebase.mapper

import com.woory.data.model.UserHpModel
import com.woory.firebase.model.UserHpDocument

internal fun UserHpDocument.asUserHpModel() = UserHpModel(
    id = this.id,
    hp = this.hp
)

internal fun UserHpModel.asUserHp() = UserHpDocument(
    id = this.id,
    hp = this.hp
)