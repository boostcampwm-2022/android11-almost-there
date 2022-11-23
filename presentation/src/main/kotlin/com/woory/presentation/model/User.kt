package com.woory.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val data: UserData
) : Parcelable

@Parcelize
data class UserData(
    val name: String,
    val profileImage: UserProfileImage
) : Parcelable

@Parcelize
data class UserProfileImage(
    val backgroundColor: String,
    val imageIndex: Int
) : Parcelable

@Parcelize
data class UserState(
    val id: String,
    val hp: Int,
    val location: Location
) : Parcelable