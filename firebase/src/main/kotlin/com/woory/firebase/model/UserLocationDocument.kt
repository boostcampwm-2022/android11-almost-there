package com.woory.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class UserLocationDocument(
    val id: String = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val updatedAt: Timestamp = Timestamp(1, 1),
)