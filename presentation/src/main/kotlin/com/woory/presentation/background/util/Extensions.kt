package com.woory.presentation.background.util

import android.content.Intent
import com.woory.presentation.model.PromiseAlarm
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

fun Intent.putPromiseAlarm(promiseAlarm: PromiseAlarm) {
    this.putExtra("alarmCode", promiseAlarm.alarmCode)
    this.putExtra("promiseCode", promiseAlarm.promiseCode)
    this.putExtra("status", promiseAlarm.status)
    this.putExtra("startTime", promiseAlarm.startTime.asMillis())
    this.putExtra("endTime", promiseAlarm.endTime.asMillis())
}

fun Intent.asPromiseAlarm(): PromiseAlarm {
    val extras = this.extras ?: throw IllegalArgumentException()
    val alarmCode = extras.getInt("alarmCode")
    val promiseCode = extras.getString("promiseCode") ?: throw IllegalArgumentException()
    val status = extras.getString("status") ?: throw IllegalArgumentException()
    val startTime = extras.getLong("startTime")
    val endTime = extras.getLong("endTime")

    return PromiseAlarm(
        alarmCode = alarmCode,
        promiseCode = promiseCode,
        status = status,
        startTime = startTime.asOffsetDateTime(),
        endTime = endTime.asOffsetDateTime()
    )
}

fun OffsetDateTime.asMillis() = this.toInstant().toEpochMilli()
fun Long.asOffsetDateTime() =
    OffsetDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())