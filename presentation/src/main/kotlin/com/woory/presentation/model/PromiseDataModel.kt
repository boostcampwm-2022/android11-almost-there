package com.woory.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

@Parcelize
data class PromiseDataModel(
    val code: String,
    val promiseLocation: LocationModel,
    val promiseDateTime: OffsetDateTime,
    val gameDateTime: OffsetDateTime,
    val host: UserModel,
    val users: List<UserModel>
) : Parcelable {

    val participant = "${host.name}" + if (users.size > 1) "외 ${users.size}명" else ""
    val promiseDetailDate =
        promiseDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분"))
}