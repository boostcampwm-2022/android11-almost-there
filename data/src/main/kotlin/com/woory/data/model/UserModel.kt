package com.woory.data.model

data class UserModel(
    val userId: String,
    val userName: String,
    val userImage: UserImage
)

data class UserImage(
    val color: String,
    val imageIdx: Int
)