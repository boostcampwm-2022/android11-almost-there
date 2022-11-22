package com.woory.database

import androidx.room.*
import com.woory.database.entity.GameTimeInfoEntity

@Dao
interface PromiseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameTime(gameTimeEntity: GameTimeInfoEntity)

    @Update
    suspend fun updateGameTime(gameTimeEntity: GameTimeInfoEntity)

    @Query("SELECT * FROM game_time_info")
    suspend fun getAll(): List<GameTimeInfoEntity>

    @Query("SELECT * From game_time_info ORDER BY datetime(start_time)")
    suspend fun getPromiseTimesSortedByStartTime(): List<GameTimeInfoEntity>

    @Query("SELECT * From game_time_info ORDER BY datetime(end_time)")
    suspend fun getPromiseTimesSortedByEndTime(): List<GameTimeInfoEntity>
}