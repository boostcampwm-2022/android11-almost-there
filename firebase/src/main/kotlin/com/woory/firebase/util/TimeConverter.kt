package com.woory.firebase.util

import com.google.firebase.Timestamp
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime

fun Timestamp.asMillis(): Long = seconds * 1000 + (nanoseconds / 1000000)

fun Timestamp.asOffsetDate(): OffsetDateTime {
    return OffsetDateTime.ofInstant(Instant.ofEpochMilli(asMillis()), Constants.zoneId)
}

fun OffsetDateTime.asTimeStamp(): Timestamp =
    Timestamp(toEpochSecond(), nano)