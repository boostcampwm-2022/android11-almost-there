package com.woory.data.model

data class UserStateModel(
    val id: String,
    val hp: Int,
    val location: LocationModel
)

data class UserLocationModel(
    val id: String,
    val location: GeoPointModel
)

data class UserHpModel(
    val id: String,
    val hp: Int
)