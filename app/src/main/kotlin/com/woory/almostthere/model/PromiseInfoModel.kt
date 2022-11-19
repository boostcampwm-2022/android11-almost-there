package com.woory.almostthere.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromiseInfoModel(
    val promiseLocation: LocationModel,
    val promiseDate: DateModel,
    val promiseTime: TimeModel,
    val gameTime: TimeModel,
    val host: UserModel,
    val users: List<UserModel>
) : Parcelable