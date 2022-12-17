package com.woory.almostthere.network.model

import com.google.firebase.Timestamp

data class UserHpDocument(
    val userId: String = "",
    val hp: Int = 100,
    val arrived: Boolean = false,
    val lost: Boolean = false,
    val updatedAt: Timestamp = Timestamp(1, 1)
)