package com.woory.data.model

data class UserModel(
    val id: String,
    val name: String,
    val image: UserImage
)

data class UserImage(
    val color: String,
    val imageIdx: Int
)