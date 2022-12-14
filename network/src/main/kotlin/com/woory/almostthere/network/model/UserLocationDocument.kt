package com.woory.almostthere.network.model

import com.google.firebase.firestore.GeoPoint
import org.threeten.bp.OffsetDateTime

data class UserLocationDocument(
    val id: String = "",
    val location: GeoPoint = GeoPoint(0.0, 0.0),
    val updatedAt: Long = OffsetDateTime.now().toInstant().toEpochMilli()
)