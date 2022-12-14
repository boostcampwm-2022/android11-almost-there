package com.woory.almostthere.database

import androidx.room.TypeConverter
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId

class OffsetDateTimeConverter {

    private val zoneId: ZoneId = ZoneId.of("Asia/Seoul")

    @TypeConverter
    fun fromEpochMillis(epochMillis: Long): OffsetDateTime =
        OffsetDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), zoneId)

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime): Long = date.toInstant().toEpochMilli()
}