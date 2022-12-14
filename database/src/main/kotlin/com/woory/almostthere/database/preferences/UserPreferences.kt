package com.woory.almostthere.database.preferences

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

data class UserPreferences(
    val userID: String,
    val createdAt: Long
) {

    companion object {
        val USER_ID = stringPreferencesKey("user_id")
        val CREATED_AT = longPreferencesKey("created_at")
    }
}