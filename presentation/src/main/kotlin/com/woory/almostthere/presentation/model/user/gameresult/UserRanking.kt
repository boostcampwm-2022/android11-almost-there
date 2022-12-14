package com.woory.almostthere.presentation.model.user.gameresult

import android.os.Parcelable
import com.woory.almostthere.presentation.model.UserData
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserRanking(
    val userId: String,
    val userData: UserData,
    val hp: Int,
    val rankingNumber: Int
) : Parcelable
