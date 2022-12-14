package com.woory.almostthere.data.model

data class UserRankingModel(
    val userId: String,
    val userData: UserDataModel,
    val hp: Int,
    val rankingNumber: Int
)