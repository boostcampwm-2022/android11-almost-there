package com.woory.presentation.model

import org.threeten.bp.OffsetDateTime

data class MagneticInfo(
    val gameCode: String,
    val centerPoint: GeoPoint,
    val radius: Double,
    val updatedAt: OffsetDateTime
)
