package com.woory.presentation.model.mapper.location

import com.woory.data.model.GeoPointModel
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.mapper.UiModelMapper

object GeoPointMapper : UiModelMapper<GeoPoint, GeoPointModel> {

    override fun asUiModel(domain: GeoPointModel): GeoPoint =
        GeoPoint(
            latitude = domain.latitude,
            longitude = domain.longitude
        )

    override fun asDomain(uiModel: GeoPoint): GeoPointModel =
        GeoPointModel(
            latitude = uiModel.latitude,
            longitude = uiModel.longitude
        )
}

fun GeoPoint.asDomain(): GeoPointModel = GeoPointMapper.asDomain(this)

fun GeoPointModel.asUiModel(): GeoPoint = GeoPointMapper.asUiModel(this)