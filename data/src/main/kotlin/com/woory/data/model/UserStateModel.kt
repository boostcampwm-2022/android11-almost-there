package com.woory.data.model

data class UserLocationModel(
    val id: String,
    val location: GeoPointModel
)

data class UserHpModel(
    val id: String,
    val hp: Int
)