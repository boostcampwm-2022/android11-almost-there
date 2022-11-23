package com.woory.presentation.model.mapper

import com.woory.presentation.model.GeoPointModel

object GeoPointMapper : UiStateMapper<GeoPointModel, com.woory.data.model.GeoPointModel> {

    override fun asUiState(domain: com.woory.data.model.GeoPointModel): GeoPointModel =
        GeoPointModel(
            latitude = domain.latitude,
            longitude = domain.longitude
        )

    override fun asDomain(uiState: GeoPointModel): com.woory.data.model.GeoPointModel =
        com.woory.data.model.GeoPointModel(
            latitude = uiState.latitude,
            longitude = uiState.longitude
        )
}

fun GeoPointModel.asDomain(): com.woory.data.model.GeoPointModel = GeoPointMapper.asDomain(this)

fun com.woory.data.model.GeoPointModel.asUiState(): GeoPointModel = GeoPointMapper.asUiState(this)