package com.woory.database.entity

import org.threeten.bp.OffsetDateTime

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_time_info")
data class GameTimeInfoEntity(
    @PrimaryKey val code: String,
    @ColumnInfo(name = "start_time") val startTime: OffsetDateTime,
    @ColumnInfo(name = "end_time") val endTime: OffsetDateTime
)