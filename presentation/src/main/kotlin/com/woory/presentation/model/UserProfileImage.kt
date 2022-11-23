package com.woory.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfileImage(
    val backgroundColor: String,
    val imageIndex: Int
) : Parcelable