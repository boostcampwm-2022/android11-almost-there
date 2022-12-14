package com.woory.almostthere.presentation.model.mapper.location

import com.woory.almostthere.data.model.GeoPointModel
import com.woory.almostthere.data.model.UserLocationModel
import com.woory.almostthere.presentation.model.GeoPoint
import com.woory.almostthere.presentation.model.UserLocation
import com.woory.almostthere.presentation.model.mapper.UiModelMapper

object UserLocationMapper : UiModelMapper<UserLocation, UserLocationModel> {
    override fun asUiModel(domain: UserLocationModel): UserLocation =
        UserLocation(
            token = domain.id,
            geoPoint = GeoPoint(
                domain.location.latitude,
                domain.location.longitude
            ),
            updatedAt = domain.updatedAt
        )

    override fun asDomain(uiModel: UserLocation): UserLocationModel =
        UserLocationModel(
            id = uiModel.token,
            location = GeoPointModel(
                uiModel.geoPoint.latitude,
                uiModel.geoPoint.longitude
            ),
            updatedAt = uiModel.updatedAt
        )
}

internal fun UserLocation.asDomain(): UserLocationModel =
    UserLocationMapper.asDomain(this)

internal fun UserLocationModel.asUiModel(): UserLocation =
    UserLocationMapper.asUiModel(this)