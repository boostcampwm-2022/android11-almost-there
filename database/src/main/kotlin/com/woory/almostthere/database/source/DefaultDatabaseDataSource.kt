package com.woory.almostthere.database.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.woory.almostthere.data.model.PromiseAlarmModel
import com.woory.almostthere.data.model.PromiseModel
import com.woory.almostthere.data.model.UserPreferencesModel
import com.woory.almostthere.data.source.DatabaseDataSource
import com.woory.almostthere.database.PromiseAlarmDao
import com.woory.almostthere.database.entity.mapper.asPromiseAlarmEntity
import com.woory.almostthere.database.entity.mapper.asPromiseAlarmModel
import com.woory.almostthere.database.preferences.UserPreferences
import com.woory.almostthere.database.preferences.mapper.asUserPreferencesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class DefaultDatabaseDataSource @Inject constructor(
    private val promiseAlarmDao: PromiseAlarmDao,
    private val dataStore: DataStore<Preferences>
) : DatabaseDataSource {

    override suspend fun setPromiseAlarmByPromiseModel(promiseModel: PromiseModel): Result<Unit> {
        return runCatching {
            promiseAlarmDao.setPromiseAlarm(promiseModel.asPromiseAlarmEntity())
        }
    }

    override suspend fun setPromiseAlarmByPromiseAlarmModel(promiseAlarmModel: PromiseAlarmModel): Result<Unit> {
        return kotlin.runCatching {
            promiseAlarmDao.setPromiseAlarm(promiseAlarmModel.asPromiseAlarmEntity())
        }
    }

    override suspend fun getAll(): Result<List<PromiseAlarmModel>> {
        return runCatching {
            promiseAlarmDao.getAll().asPromiseAlarmModel()
        }
    }

    override suspend fun getPromiseAlarmSortedByStartTime(): Result<List<PromiseAlarmModel>> {
        return runCatching {
            promiseAlarmDao.getPromiseAlarmSortedByStartTime().asPromiseAlarmModel()
        }
    }

    override suspend fun getPromiseAlarmSortedByEndTime(): Result<List<PromiseAlarmModel>> {
        return runCatching {
            promiseAlarmDao.getPromiseAlarmSortedByEndTime().asPromiseAlarmModel()
        }
    }

    override suspend fun getPromiseAlarmWhereCode(promiseCode: String): Result<PromiseAlarmModel> {
        return runCatching {
            promiseAlarmDao.getPromiseAlarmWhereCode(promiseCode).asPromiseAlarmModel()
        }
    }

    override fun getJoinedPromises(): Flow<List<PromiseAlarmModel>> =
        promiseAlarmDao.getJoinedPromises().map { entities ->
            entities.asPromiseAlarmModel()
        }

    override fun getUserPreferences(): Flow<UserPreferencesModel> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val userID = preferences[UserPreferences.USER_ID]
            val createAt =
                preferences[UserPreferences.CREATED_AT] ?: System.currentTimeMillis()

            if (userID == null) {
                val newUserID = UUID.randomUUID().toString()
                val currentTimeMillis = System.currentTimeMillis()

                dataStore.edit { userPreferences ->
                    userPreferences[UserPreferences.USER_ID] = newUserID
                    userPreferences[UserPreferences.CREATED_AT] = currentTimeMillis
                }

                UserPreferences(
                    userID = newUserID,
                    createdAt = currentTimeMillis
                ).asUserPreferencesModel()
            } else {
                UserPreferences(userID = userID, createdAt = createAt).asUserPreferencesModel()
            }
        }
}