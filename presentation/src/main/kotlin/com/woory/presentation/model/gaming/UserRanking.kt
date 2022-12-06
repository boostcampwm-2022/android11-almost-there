package com.woory.presentation.model.gaming

import com.woory.presentation.model.UserProfileImage

data class UserRanking(
    val userId: String,
    val rank: Int,
    val profileImage: UserProfileImage,
    val userName: String,
    val hp: Int
)