package com.woory.database.datasource

import com.woory.data.model.PromiseAlarmModel
import com.woory.data.model.PromiseModel
import com.woory.data.source.DatabaseDataSource
import com.woory.database.PromiseAlarmDao
import com.woory.database.mapper.toPromiseAlarmEntity
import com.woory.database.mapper.toPromiseAlarmModel

class DefaultDatabaseDataSource(private val dao: PromiseAlarmDao) : DatabaseDataSource {

    override suspend fun setPromiseAlarm(promiseModel: PromiseModel): Result<Unit> {
        return runCatching {
            dao.setPromiseAlarm(promiseModel.toPromiseAlarmEntity())
        }
    }

    override suspend fun getAll(): Result<List<PromiseAlarmModel>> {
        return runCatching {
            dao.getAll().toPromiseAlarmModel()
        }
    }

    override suspend fun getPromiseAlarmSortedByStartTime(): Result<List<PromiseAlarmModel>> {
        return runCatching {
            dao.getPromiseAlarmSortedByStartTime().toPromiseAlarmModel()
        }
    }

    override suspend fun getPromiseAlarmSortedByEndTime(): Result<List<PromiseAlarmModel>> {
        return runCatching {
            dao.getPromiseAlarmSortedByEndTime().toPromiseAlarmModel()
        }
    }

    override suspend fun getPromiseAlarmWhereCode(promiseCode: String): Result<PromiseAlarmModel> {
        return runCatching {
            dao.getPromiseAlarmWhereCode(promiseCode).toPromiseAlarmModel()
        }
    }
}