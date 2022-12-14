package com.woory.almostthere.data.source

import com.woory.almostthere.data.model.PromiseAlarmModel
import com.woory.almostthere.data.model.PromiseModel
import com.woory.almostthere.data.model.UserPreferencesModel
import kotlinx.coroutines.flow.Flow

interface DatabaseDataSource {

    suspend fun setPromiseAlarmByPromiseModel(promiseModel: PromiseModel): Result<Unit>

    suspend fun setPromiseAlarmByPromiseAlarmModel(promiseAlarmModel: PromiseAlarmModel): Result<Unit>

    suspend fun getAll(): Result<List<PromiseAlarmModel>>

    suspend fun getPromiseAlarmSortedByStartTime(): Result<List<PromiseAlarmModel>>

    suspend fun getPromiseAlarmSortedByEndTime(): Result<List<PromiseAlarmModel>>

    suspend fun getPromiseAlarmWhereCode(promiseCode: String): Result<PromiseAlarmModel>

    fun getJoinedPromises(): Flow<List<PromiseAlarmModel>>

    fun getUserPreferences(): Flow<UserPreferencesModel>
}