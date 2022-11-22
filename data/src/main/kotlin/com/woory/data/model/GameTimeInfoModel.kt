package com.woory.data.model

import org.threeten.bp.OffsetDateTime

data class GameTimeInfoModel(
    val code: String,
    val startTime: OffsetDateTime,
    val endTime: OffsetDateTime
)
