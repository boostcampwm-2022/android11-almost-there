package com.woory.almostthere.data.model

data class UserModel(
    val userId: String,
    val data: UserDataModel
)

data class UserDataModel(
    val name: String,
    val profileImage: UserProfileImageModel
)

data class UserProfileImageModel(
    val color: String,
    val imageIndex: Int
)