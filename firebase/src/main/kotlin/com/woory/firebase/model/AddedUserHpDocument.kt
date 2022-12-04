package com.woory.firebase.model

import com.google.firebase.Timestamp

data class AddedUserHpDocument(
    val userId: String = "",
    val hp: Int = 100,
    val arrived: Boolean = false,
    val lost: Boolean = false,
    val updatedAt: Timestamp = Timestamp(1, 1)
)