package com.woory.almostthere.network.model.mapper

import com.woory.almostthere.data.model.AddedUserHpModel
import com.woory.almostthere.network.model.AddedUserHpDocument
import com.woory.almostthere.network.util.TimeConverter.asOffsetDate
import com.woory.almostthere.network.util.TimeConverter.asTimeStamp

object AddedUserHpMapper : ModelMapper<AddedUserHpModel, AddedUserHpDocument> {
    override fun asModel(domain: AddedUserHpModel): AddedUserHpDocument =
        AddedUserHpDocument(
            userId = domain.userId,
            hp = domain.hp,
            arrived = domain.arrived,
            lost = domain.lost,
            updatedAt = domain.updatedAt.asTimeStamp()
        )

    override fun asDomain(model: AddedUserHpDocument): AddedUserHpModel =
        AddedUserHpModel(
            userId = model.userId,
            hp = model.hp,
            arrived = model.arrived,
            lost = model.lost,
            updatedAt = model.updatedAt.asOffsetDate()
        )
}

internal fun AddedUserHpModel.asModel() = AddedUserHpMapper.asModel(this)

internal fun AddedUserHpDocument.asDomain() = AddedUserHpMapper.asDomain(this)