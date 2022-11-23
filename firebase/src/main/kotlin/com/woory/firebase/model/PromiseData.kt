package com.woory.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Promise(
    val address: String = "",
    val code: String = "",
    val destination: GeoPoint = GeoPoint(0.0, 0.0),
    val host: PromiseParticipant = PromiseParticipant(),
    val gameTime: Timestamp = Timestamp(1, 1),
    val promiseTime: Timestamp = Timestamp(1, 1),
    val users: List<PromiseParticipant> = listOf()
)

data class PromiseParticipant(
    val userImage: UserImageInfo = UserImageInfo(),
    val userName: String = "",
    val userId: String = ""
)

data class UserImageInfo(
    val color: String = "",
    val imageIdx: Int = 0
)