package com.woory.data.model

data class UserLocationModel(
    val id: String,
    val location: GeoPointModel,
    val updatedAt: Long,
)