package com.woory.almostthere.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ReadyUser(
    val isReady: Boolean,
    val user: User,
)

@Parcelize
data class User(
    val userId: String,
    val data: UserData
) : Parcelable

@Parcelize
data class UserData(
    val name: String,
    val profileImage: UserProfileImage
) : Parcelable

@Parcelize
data class UserProfileImage(
    val color: String,
    val imageIndex: Int
) : Parcelable

@Parcelize
data class UserState(
    val userId: String,
    val hp: Int,
    val location: Location
) : Parcelable

@Parcelize
data class UserHp(
    val userId: String,
    val hp: Int
) : Parcelable
