package com.woory.data.source

import com.woory.data.model.PromiseAlarmModel
import com.woory.data.model.PromiseModel

interface DatabaseDataSource {
    suspend fun setPromiseAlarmByPromiseModel(promiseModel: PromiseModel): Result<Unit>

    suspend fun setPromiseAlarmByPromiseAlarmModel(promiseAlarmModel: PromiseAlarmModel): Result<Unit>

    suspend fun getAll(): Result<List<PromiseAlarmModel>>

    suspend fun getPromiseAlarmSortedByStartTime(): Result<List<PromiseAlarmModel>>

    suspend fun getPromiseAlarmSortedByEndTime(): Result<List<PromiseAlarmModel>>

    suspend fun getPromiseAlarmWhereCode(promiseCode: String): Result<PromiseAlarmModel>
}