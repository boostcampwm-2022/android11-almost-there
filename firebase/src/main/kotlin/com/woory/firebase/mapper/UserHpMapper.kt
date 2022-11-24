package com.woory.firebase.mapper

import com.woory.data.model.UserHpModel
import com.woory.firebase.model.UserHpDocument

object UserHpMapper : ModelMapper<UserHpModel, UserHpDocument> {
    override fun asModel(domain: UserHpModel): UserHpDocument =
        UserHpDocument(
            id = domain.id,
            hp = domain.hp
        )

    override fun asDomain(model: UserHpDocument): UserHpModel = UserHpModel(
        id = model.id,
        hp = model.hp
    )
}

internal fun UserHpModel.asModel() = UserHpMapper.asModel(this)

internal fun UserHpDocument.asDomain() = UserHpMapper.asDomain(this)

//internal fun UserHpDocument.asUserHpModel() = UserHpModel(
//    id = this.id,
//    hp = this.hp
//)
//
//internal fun UserHpModel.asUserHp() = UserHpDocument(
//    id = this.id,
//    hp = this.hp
//)