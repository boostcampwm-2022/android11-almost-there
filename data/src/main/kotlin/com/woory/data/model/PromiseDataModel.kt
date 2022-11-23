package com.woory.data.model

import org.threeten.bp.OffsetDateTime

data class PromiseDataModel(
    val promiseLocation: LocationModel,
    val promiseDateTime: OffsetDateTime,
    val gameDateTime: OffsetDateTime,
    val host: UserModel,
    val users: List<UserModel>
)