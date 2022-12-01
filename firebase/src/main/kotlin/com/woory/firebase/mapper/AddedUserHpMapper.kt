package com.woory.firebase.mapper

import com.woory.data.model.AddedUserHpModel
import com.woory.firebase.model.AddedUserHpDocument
import com.woory.firebase.util.TimeConverter.asOffsetDate
import com.woory.firebase.util.TimeConverter.asTimeStamp

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