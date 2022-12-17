package com.woory.almostthere.data.model

data class PromiseHistoryModel(
    val promise: PromiseModel,
    val magnetic: MagneticInfoModel? = null,
    val users: List<UserHpModel>? = null
)