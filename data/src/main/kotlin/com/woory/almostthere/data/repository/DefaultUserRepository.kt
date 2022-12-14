package com.woory.almostthere.data.repository

import com.woory.almostthere.data.model.UserPreferencesModel
import com.woory.almostthere.data.source.DatabaseDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
    private val databaseDataSource: DatabaseDataSource,
) : UserRepository {

    override val userPreferences: Flow<UserPreferencesModel> =
        databaseDataSource.getUserPreferences()
}