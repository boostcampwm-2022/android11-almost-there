package com.woory.almostthere.presentation.model.mapper.location

import com.woory.almostthere.data.model.LocationModel
import com.woory.almostthere.presentation.model.Location
import com.woory.almostthere.presentation.model.mapper.UiModelMapper

object LocationMapper : UiModelMapper<Location, LocationModel> {

    override fun asUiModel(domain: LocationModel): Location =
        Location(
            geoPoint = domain.geoPoint.asUiModel(),
            address = domain.address
        )

    override fun asDomain(uiModel: Location): LocationModel =
        LocationModel(
            geoPoint = uiModel.geoPoint.asDomain(),
            address = uiModel.address
        )
}

fun Location.asDomain(): LocationModel = LocationMapper.asDomain(this)

fun LocationModel.asUiModel(): Location = LocationMapper.asUiModel(this)