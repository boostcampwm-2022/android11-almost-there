package com.woory.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class PromiseData(
    val Destination: GeoPoint,
    val Address: String,
    val GameTime: Timestamp,
    val PromiseTime: Timestamp,
    val Host: Map<String, String>,
    val Users: List<PromiseParticipant>
)

data class PromiseParticipant(
    val UserImage: Map<String, String>,
    val UserName: String,
    val UserToken: String
)