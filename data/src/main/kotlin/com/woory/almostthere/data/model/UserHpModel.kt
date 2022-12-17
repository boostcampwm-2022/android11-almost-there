package com.woory.almostthere.data.model

import org.threeten.bp.OffsetDateTime

data class UserHpModel(
    val userId: String,
    val hp: Int,
    val arrived: Boolean,
    val lost: Boolean,
    val updatedAt: OffsetDateTime
)