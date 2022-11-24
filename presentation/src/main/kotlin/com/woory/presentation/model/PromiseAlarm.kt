package com.woory.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.OffsetDateTime

@Parcelize
data class PromiseAlarm(
    val alarmCode: Int,
    val promiseCode: String,
    var status: String,
    val startTime: OffsetDateTime,
    val endTime: OffsetDateTime
) : Parcelable {
    fun onStatus(statusValue: String) =
        this.copy().apply {
            status = statusValue
        }
}
