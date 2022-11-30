package com.woory.firebase.mapper

import com.google.firebase.firestore.GeoPoint
import com.woory.data.model.GeoPointModel
import com.woory.data.model.UserLocationModel
import com.woory.firebase.model.UserLocationDocument
import com.woory.firebase.util.TimeConverter.asTimeStamp

object UserLocationMapper : ModelMapper<UserLocationModel, UserLocationDocument> {
    override fun asModel(domain: UserLocationModel): UserLocationDocument = UserLocationDocument(
        id = domain.id,
        location = GeoPoint(domain.location.latitude, domain.location.longitude)
    )

    override fun asDomain(model: UserLocationDocument): UserLocationModel = UserLocationModel(
        id = model.id,
        location = GeoPointModel(model.location.latitude, model.location.longitude)
    )
}

internal fun UserLocationModel.asModel() = UserLocationMapper.asModel(this)

internal fun UserLocationDocument.asDomain() = UserLocationMapper.asDomain(this)