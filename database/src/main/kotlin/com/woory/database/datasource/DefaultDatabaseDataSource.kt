package com.woory.database.datasource

import com.woory.data.model.GameTimeInfoModel
import com.woory.data.source.DatabaseDataSource
import com.woory.database.PromiseDao
import com.woory.database.mapper.toGameTimeInfoEntity
import com.woory.database.mapper.toGameTimeInfoModel
import com.woory.database.mapper.toGameTimeInfoModels

class DefaultDatabaseDataSource(private val dao: PromiseDao) : DatabaseDataSource {

    override suspend fun insertGameTime(gameTimeInfo: GameTimeInfoModel): Result<Unit> {
        return runCatching {
            dao.insertGameTime(gameTimeInfo.toGameTimeInfoEntity())
        }
    }

    override suspend fun updateGameTime(gameTimeInfo: GameTimeInfoModel): Result<Unit> {
        return runCatching {
            dao.updateGameTime(gameTimeInfo.toGameTimeInfoEntity())
        }
    }

    override suspend fun getAll(): Result<List<GameTimeInfoModel>> {
        return runCatching {
            dao.getAll().toGameTimeInfoModels()
        }
    }

    override suspend fun getGameTimesSortedByStartTime(): Result<List<GameTimeInfoModel>> {
        return runCatching {
            dao.getPromiseTimesSortedByStartTime().toGameTimeInfoModels()
        }
    }

    override suspend fun getGameTimesSortedByEndTime(): Result<List<GameTimeInfoModel>> {
        return runCatching {
            dao.getPromiseTimesSortedByEndTime().toGameTimeInfoModels()
        }
    }

    override suspend fun getGameTimeByCode(code: String): Result<GameTimeInfoModel> = runCatching {
        val result = dao.getGameTimeByCode(code) ?: throw NoSuchElementException()

        result.toGameTimeInfoModel()
    }
}