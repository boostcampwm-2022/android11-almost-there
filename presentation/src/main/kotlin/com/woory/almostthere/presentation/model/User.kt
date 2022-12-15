package com.woory.almostthere.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReadyUser(
    val isReady: Boolean,
    val user: User,
) : Parcelable

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
