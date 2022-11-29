package com.woory.database

import androidx.room.TypeConverter
import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

object OffsetDateTimeConverters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun toOffsetDateTime(value: Long?): OffsetDateTime? {
        return value?.let {
            return OffsetDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
        }
    }

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?): Long? {
        return date?.toInstant()?.toEpochMilli()
    }
}