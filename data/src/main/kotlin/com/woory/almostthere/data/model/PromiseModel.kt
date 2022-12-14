package com.woory.almostthere.data.model

import org.threeten.bp.OffsetDateTime

data class PromiseModel(
    val code: String,
    val data: PromiseDataModel
)

data class PromiseDataModel(
    val promiseLocation: LocationModel,
    val promiseDateTime: OffsetDateTime,
    val gameDateTime: OffsetDateTime,
    val host: UserModel,
    val users: List<UserModel>
)