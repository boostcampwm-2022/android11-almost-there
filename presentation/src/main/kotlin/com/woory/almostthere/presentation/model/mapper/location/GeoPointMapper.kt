package com.woory.almostthere.presentation.model.mapper.location

import com.woory.almostthere.data.model.GeoPointModel
import com.woory.almostthere.presentation.model.GeoPoint
import com.woory.almostthere.presentation.model.mapper.UiModelMapper

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