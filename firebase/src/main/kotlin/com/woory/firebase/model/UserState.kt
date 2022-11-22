package com.woory.firebase.model

import com.google.firebase.firestore.GeoPoint

data class UserState(
    val id: String,
    val hp: Int,
    val location: GeoPoint,
    val address: String
)