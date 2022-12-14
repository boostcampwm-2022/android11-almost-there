package com.woory.almostthere.data.model

import org.threeten.bp.OffsetDateTime

data class MagneticInfoModel(
    val gameCode: String,
    val centerPoint: GeoPointModel,
    val radius: Double,
    val initialRadius: Double,
    val updatedAt: OffsetDateTime
)