package com.woory.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.OffsetDateTime

@Parcelize
data class PromiseAlarm(
    val alarmCode: Int,
    val promiseCode: String,
    val state: AlarmState,
    val startTime: OffsetDateTime,
    val endTime: OffsetDateTime
) : Parcelable
