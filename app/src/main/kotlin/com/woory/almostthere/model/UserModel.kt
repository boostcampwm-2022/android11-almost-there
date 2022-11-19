package com.woory.almostthere.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class UserModel(
    val name: String,
    val image: String
): Parcelable