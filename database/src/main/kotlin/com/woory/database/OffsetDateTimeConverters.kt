package com.woory.database

import androidx.room.TypeConverter
import com.woory.database.util.TimeConverter.asMillis
import com.woory.database.util.TimeConverter.asOffsetDateTime
import org.threeten.bp.OffsetDateTime

object OffsetDateTimeConverters {
    @TypeConverter
    fun toOffsetDateTime(value: Long?): OffsetDateTime? {
        return value?.asOffsetDateTime()
    }

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?): Long? {
        return date?.asMillis()
    }
}

