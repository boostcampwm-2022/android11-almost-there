package com.woory.presentation.model.mapper.location

import com.woory.data.model.GeoPointModel
import com.woory.data.model.UserLocationModel
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.UserLocation
import com.woory.presentation.model.mapper.UiModelMapper

object UserLocationMapper : UiModelMapper<UserLocation, UserLocationModel> {
    override fun asUiModel(domain: UserLocationModel): UserLocation =
        UserLocation(
            token = domain.id,
            geoPoint = GeoPoint(
                domain.location.latitude,
                domain.location.longitude
            )
        )

    override fun asDomain(uiModel: UserLocation): UserLocationModel =
        UserLocationModel(
            id = uiModel.token,
            location = GeoPointModel(
                uiModel.geoPoint.latitude,
                uiModel.geoPoint.longitude
            )
        )
}

internal fun UserLocation.asDomain(): UserLocationModel =
    UserLocationMapper.asDomain(this)

internal fun UserLocationModel.asUiModel(): UserLocation =
    UserLocationMapper.asUiModel(this)