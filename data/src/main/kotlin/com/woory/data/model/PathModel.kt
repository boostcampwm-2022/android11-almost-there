package com.woory.data.model

import java.time.OffsetDateTime

data class PathModel(
    val points: List<GeoPointModel>,
    val time: OffsetDateTime,
)