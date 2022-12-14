package com.woory.almostthere.presentation.model

import org.threeten.bp.OffsetDateTime

data class AddedUserHp(
    val userId: String,
    val hp: Int,
    val arrived: Boolean,
    val lost: Boolean,
    val updatedAt: OffsetDateTime
)