package com.woory.almostthere.data.model

data class UserLocationModel(
    val id: String,
    val location: GeoPointModel,
    val updatedAt: Long,
)