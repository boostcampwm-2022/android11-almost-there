package com.woory.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class PromiseDocument(
    val address: String = "",
    val code: String = "",
    val destination: GeoPoint = GeoPoint(0.0, 0.0),
    val host: PromiseParticipantField = PromiseParticipantField(),
    val gameTime: Timestamp = Timestamp(1, 1),
    val promiseTime: Timestamp = Timestamp(1, 1),
    val users: List<PromiseParticipantField> = listOf(),
    val finished: Boolean = false
)

data class PromiseParticipantField(
    val userImage: UserImageInfoField = UserImageInfoField(),
    val userName: String = "",
    val userId: String = ""
)

data class UserImageInfoField(
    val color: String = "",
    val imageIdx: Int = 0
)