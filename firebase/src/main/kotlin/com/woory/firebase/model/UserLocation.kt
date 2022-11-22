package com.woory.firebase.model

import com.google.firebase.firestore.GeoPoint

data class UserLocation(
    val Id: String,
    val Location: GeoPoint
)