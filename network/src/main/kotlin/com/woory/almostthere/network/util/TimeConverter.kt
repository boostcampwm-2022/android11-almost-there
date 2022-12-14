package com.woory.almostthere.network.util

import com.google.firebase.Timestamp
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

object TimeConverter {

    private val zoneId: ZoneId = ZoneId.of("Asia/Seoul")

    fun Timestamp.asMillis(): Long = seconds * 1000 + (nanoseconds / 1000000)

    fun Timestamp.asOffsetDate(): OffsetDateTime {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(asMillis()), zoneId)
    }

    fun Long.asTimeStamp() = Timestamp(this / 1000, (this % 1000).toInt() * 1000000)

    fun OffsetDateTime.asTimeStamp(): Timestamp =
        Timestamp(toEpochSecond(), nano)
}