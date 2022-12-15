package com.woory.almostthere.network.model.mapper

import com.woory.almostthere.data.model.UserHpModel
import com.woory.almostthere.network.model.UserHpDocument
import com.woory.almostthere.network.util.TimeConverter.asOffsetDate
import com.woory.almostthere.network.util.TimeConverter.asTimeStamp

object AddedUserHpMapper : ModelMapper<UserHpModel, UserHpDocument> {
    override fun asModel(domain: UserHpModel): UserHpDocument =
        UserHpDocument(
            userId = domain.userId,
            hp = domain.hp,
            arrived = domain.arrived,
            lost = domain.lost,
            updatedAt = domain.updatedAt.asTimeStamp()
        )

    override fun asDomain(model: UserHpDocument): UserHpModel =
        UserHpModel(
            userId = model.userId,
            hp = model.hp,
            arrived = model.arrived,
            lost = model.lost,
            updatedAt = model.updatedAt.asOffsetDate()
        )
}

internal fun UserHpModel.asModel() = AddedUserHpMapper.asModel(this)

internal fun UserHpDocument.asDomain() = AddedUserHpMapper.asDomain(this)