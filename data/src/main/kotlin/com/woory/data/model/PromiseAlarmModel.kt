package com.woory.data.model

import org.threeten.bp.OffsetDateTime

data class PromiseAlarmModel(
    val alarmCode: Int,
    val promiseCode: String,
    val status: String,
    val startTime: OffsetDateTime,
    val endTime: OffsetDateTime
)