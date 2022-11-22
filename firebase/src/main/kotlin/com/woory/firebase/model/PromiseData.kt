package com.woory.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class PromiseData(
    val Address: String,
    val Code: String,
    val Destination: GeoPoint,
    val Host: PromiseParticipant,
    val GameTime: Timestamp,
    val PromiseTime: Timestamp,
    val Users: List<PromiseParticipant>
)

data class PromiseParticipant(
    val UserImage: UserImageInfo,
    val UserName: String,
    val UserId: String
)

data class UserImageInfo(
    val Color: String,
    val ImageIdx: Int
)