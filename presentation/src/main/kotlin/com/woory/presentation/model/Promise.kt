package com.woory.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.OffsetDateTime

@Parcelize
data class Promise(
    val code: String,
    val data: PromiseData
) : Parcelable

@Parcelize
data class PromiseData(
    val promiseLocation: Location,
    val promiseDateTime: OffsetDateTime,
    val gameDateTime: OffsetDateTime,
    val host: User,
    val users: List<User>
) : Parcelable