package com.woory.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.woory.data.model.UserPreferencesModel
import com.woory.data.model.UserPreferencesModel.Companion.CREATED_AT
import com.woory.data.model.UserPreferencesModel.Companion.USER_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserRepository {

    override val userPreferences: Flow<UserPreferencesModel> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val userID = preferences[USER_ID]
            val createAt = preferences[CREATED_AT] ?: System.currentTimeMillis()

            if (userID == null) {
                val newUserID = UUID.randomUUID().toString()
                val currentTimeMillis = System.currentTimeMillis()

                dataStore.edit { userPreferences ->
                    userPreferences[USER_ID] = newUserID
                    userPreferences[CREATED_AT] = currentTimeMillis
                }

                UserPreferencesModel(userID = newUserID, createdAt = currentTimeMillis)
            } else {
                UserPreferencesModel(userID = userID, createdAt = createAt)
            }
        }
}