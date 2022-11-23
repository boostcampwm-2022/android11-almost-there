package com.woory.presentation.model.mapper

import com.woory.presentation.model.LocationModel

object LocationMapper : UiStateMapper<LocationModel, com.woory.data.model.LocationModel> {

    override fun asUiState(domain: com.woory.data.model.LocationModel): LocationModel =
        LocationModel(
            geoPoint = domain.geoPoint.asUiState(),
            address = domain.address
        )

    override fun asDomain(uiState: LocationModel): com.woory.data.model.LocationModel =
        com.woory.data.model.LocationModel(
            geoPoint = uiState.geoPoint.asDomain(),
            address = uiState.address
        )
}

fun LocationModel.asDomain(): com.woory.data.model.LocationModel = LocationMapper.asDomain(this)

fun com.woory.data.model.LocationModel.asUiState(): LocationModel = LocationMapper.asUiState(this)