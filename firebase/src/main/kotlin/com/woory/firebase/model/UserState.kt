package com.woory.firebase.model

import com.google.firebase.firestore.GeoPoint

data class UserState(
    val id: String = "",
    val hp: Int = 0,
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val address: String = ""
)