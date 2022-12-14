package com.woory.almostthere.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.woory.almostthere.database.entity.PromiseAlarmEntity

@Database(
    entities = [PromiseAlarmEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(value = [OffsetDateTimeConverter::class])
abstract class AlmostThereDatabase : RoomDatabase() {

    abstract fun promiseAlarmDao(): PromiseAlarmDao
}