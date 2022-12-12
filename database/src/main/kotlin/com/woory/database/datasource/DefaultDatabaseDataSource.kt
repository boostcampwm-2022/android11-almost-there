package com.woory.database.datasource

import com.woory.data.model.PromiseAlarmModel
import com.woory.data.model.PromiseModel
import com.woory.data.source.DatabaseDataSource
import com.woory.database.PromiseAlarmDao
import com.woory.database.mapper.asPromiseAlarmEntity
import com.woory.database.mapper.asPromiseAlarmModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultDatabaseDataSource(private val dao: PromiseAlarmDao) : DatabaseDataSource {

    override suspend fun setPromiseAlarmByPromiseModel(promiseModel: PromiseModel): Result<Unit> {
        return runCatching {
            dao.setPromiseAlarm(promiseModel.asPromiseAlarmEntity())
        }
    }

    override suspend fun setPromiseAlarmByPromiseAlarmModel(promiseAlarmModel: PromiseAlarmModel): Result<Unit> {
        return kotlin.runCatching {
            dao.setPromiseAlarm(promiseAlarmModel.asPromiseAlarmEntity())
        }
    }

    override suspend fun getAll(): Result<List<PromiseAlarmModel>> {
        return runCatching {
            dao.getAll().asPromiseAlarmModel()
        }
    }

    override suspend fun getPromiseAlarmSortedByStartTime(): Result<List<PromiseAlarmModel>> {
        return runCatching {
            dao.getPromiseAlarmSortedByStartTime().asPromiseAlarmModel()
        }
    }

    override suspend fun getPromiseAlarmSortedByEndTime(): Result<List<PromiseAlarmModel>> {
        return runCatching {
            dao.getPromiseAlarmSortedByEndTime().asPromiseAlarmModel()
        }
    }

    override suspend fun getPromiseAlarmWhereCode(promiseCode: String): Result<PromiseAlarmModel> {
        return runCatching {
            dao.getPromiseAlarmWhereCode(promiseCode).asPromiseAlarmModel()
        }
    }

    override fun getJoinedPromises(): Flow<List<PromiseAlarmModel>> =
        dao.getJoinedPromises().map { entities ->
            entities.asPromiseAlarmModel()
        }
}