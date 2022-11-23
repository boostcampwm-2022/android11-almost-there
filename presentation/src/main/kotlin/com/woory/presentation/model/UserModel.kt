package com.woory.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val name: String,
    val profileImage: UserProfileImage
) : Parcelable