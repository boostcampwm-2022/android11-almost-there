package com.woory.almostthere.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "promise_alarm")
data class PromiseAlarmEntity(
    @PrimaryKey(autoGenerate = true) val alarmCode: Int = 0,
    @ColumnInfo val promiseCode: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "start_time") val startTime: OffsetDateTime,
    @ColumnInfo(name = "end_time") val endTime: OffsetDateTime
)