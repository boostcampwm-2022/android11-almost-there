package com.woory.data.model

data class UserModel(
    val id: String,
    val data: UserDataModel
)

data class UserDataModel(
    val name: String,
    val profileImage: UserProfileImageModel
)

data class UserProfileImageModel(
    val backgroundColor: String,
    val imageIndex: Int
)