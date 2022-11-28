package com.woory.firebase.util

import com.google.firebase.Timestamp
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import java.util.*

object TimeConverter {

    val zoneId: ZoneId = ZoneId.of("Asia/Seoul")
    val zoneOffset: ZoneOffset = ZoneOffset.of("+09:00")
    val locale: Locale = Locale.KOREA

    fun Timestamp.asMillis(): Long = seconds * 1000 + (nanoseconds / 1000000)

    fun Timestamp.asOffsetDate(): OffsetDateTime {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(asMillis()), zoneId)
    }

    fun OffsetDateTime.asTimeStamp(): Timestamp =
        Timestamp(toEpochSecond(), nano)
}