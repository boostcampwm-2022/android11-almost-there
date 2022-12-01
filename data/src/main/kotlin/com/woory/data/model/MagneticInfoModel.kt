package com.woory.data.model

import org.threeten.bp.OffsetDateTime

data class MagneticInfoModel(
    val gameCode: String,
    val centerPoint: GeoPointModel,
    val radius: Double,
    val updatedAt: OffsetDateTime
)