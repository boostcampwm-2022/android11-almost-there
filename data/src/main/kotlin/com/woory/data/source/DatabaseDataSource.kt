package com.woory.data.source

import com.woory.data.model.GameTimeInfoModel

interface DatabaseDataSource {
    suspend fun insertGameTime(gameTimeInfo: GameTimeInfoModel): Result<Unit>

    suspend fun updateGameTime(gameTimeInfo: GameTimeInfoModel): Result<Unit>

    suspend fun getAll(): Result<List<GameTimeInfoModel>>

    suspend fun getGameTimesSortedByStartTime(): Result<List<GameTimeInfoModel>>

    suspend fun getGameTimesSortedByEndTime(): Result<List<GameTimeInfoModel>>

    suspend fun getGameTimeByCode(code: String): Result<GameTimeInfoModel>
}