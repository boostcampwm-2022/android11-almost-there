package com.woory.data.repository

import com.woory.data.model.UserPreferencesModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val userPreferences: Flow<UserPreferencesModel>
}