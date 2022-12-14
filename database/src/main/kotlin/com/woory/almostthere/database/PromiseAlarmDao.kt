package com.woory.almostthere.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.woory.almostthere.database.entity.PromiseAlarmEntity
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.OffsetDateTime

@Dao
interface PromiseAlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPromiseAlarm(gameTimeEntity: PromiseAlarmEntity)

    @Query("SELECT * FROM promise_alarm WHERE end_time > :currentTime")
    suspend fun getAll(
        currentTime: Long = OffsetDateTime.now().toInstant().toEpochMilli()
    ): List<PromiseAlarmEntity>

    @Query("SELECT * From promise_alarm ORDER BY datetime(start_time)")
    suspend fun getPromiseAlarmSortedByStartTime(): List<PromiseAlarmEntity>

    @Query("SELECT * From promise_alarm ORDER BY datetime(end_time)")
    suspend fun getPromiseAlarmSortedByEndTime(): List<PromiseAlarmEntity>

    @Query("SELECT * FROM promise_alarm WHERE promiseCode=:promiseCode")
    suspend fun getPromiseAlarmWhereCode(promiseCode: String): PromiseAlarmEntity

    @Query("SELECT * FROM promise_alarm ORDER BY end_time ASC")
    fun getJoinedPromises(): Flow<List<PromiseAlarmEntity>>
}