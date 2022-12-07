package com.woory.data.model

data class UserRankingModel(
    val userId: String,
    val userData: UserDataModel,
    val hp: Int,
    val rankingNumber: Int
)