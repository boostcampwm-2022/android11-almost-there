package com.woory.almostthere.database.preferences.mapper

import com.woory.almostthere.data.model.UserPreferencesModel
import com.woory.almostthere.database.preferences.UserPreferences

fun UserPreferences.asUserPreferencesModel() =
    UserPreferencesModel(
        userID = userID,
        createdAt = createdAt
    )

fun UserPreferencesModel.asUserPreferences() =
    UserPreferences(
        userID = userID,
        createdAt = createdAt
    )