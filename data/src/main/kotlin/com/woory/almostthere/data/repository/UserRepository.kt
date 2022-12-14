package com.woory.almostthere.data.repository

import com.woory.almostthere.data.model.UserPreferencesModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val userPreferences: Flow<UserPreferencesModel>
}