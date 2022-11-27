package com.woory.data.model

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

data class UserPreferencesModel(
    val userID: String,
    val createdAt: Long
) {

    companion object {
        val USER_ID = stringPreferencesKey("user_id")
        val CREATED_AT = longPreferencesKey("created_at")
    }
}