package com.woory.data.model

data class PromiseHistoryModel(
    val promise: PromiseModel,
    val magnetic: MagneticInfoModel? = null,
    val users: List<AddedUserHpModel>? = null
)