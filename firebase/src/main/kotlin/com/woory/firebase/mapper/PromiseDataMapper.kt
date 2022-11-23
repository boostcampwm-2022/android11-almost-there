package com.woory.firebase.mapper

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.woory.data.model.*
import com.woory.firebase.model.PromiseDocument
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import java.util.*

internal fun PromiseDocument.asPromiseModel(): PromiseModel {

    val code = this.code

    val promiseLocation = LocationModel(
        geoPoint = GeoPointModel(this.destination.latitude, this.destination.longitude),
        address = this.address
    )

    val promiseDateTime = this.promiseTime.toDate().asOffsetDate()
    val gameDateTime = this.gameTime.toDate().asOffsetDate()

    val host = UserModel(
        id = host.userId,
        UserDataModel(
            name = host.userName,
            profileImage = UserProfileImageModel(host.userImage.color, host.userImage.imageIdx)
        )
    )

    val users = this.users.map { it.asUserModel() }

    return PromiseModel(
        code, PromiseDataModel(promiseLocation, promiseDateTime, gameDateTime, host, users)
    )
}

internal fun PromiseDataModel.asPromiseDocument(code: String): PromiseDocument {
    val address = this.promiseLocation.address
    val destination = GeoPoint(
        this.promiseLocation.geoPoint.latitude,
        this.promiseLocation.geoPoint.longitude
    )
    val gameTime = this.gameDateTime.asTimeStamp()
    val promiseTime = this.promiseDateTime.asTimeStamp()
    val host = this.host.asPromiseParticipant()
    val users = this.users.map { it.asPromiseParticipant() }

    return PromiseDocument(
        code = code,
        address = address,
        destination = destination,
        host = host,
        gameTime = gameTime,
        promiseTime = promiseTime,
        users = users
    )
}

private fun OffsetDateTime.asTimeStamp(): Timestamp =
    Timestamp(Date(this.toInstant().toEpochMilli()))


private fun Date.asOffsetDate(): OffsetDateTime {
    val calenderDate = Calendar.getInstance()
    calenderDate.time = this

    return OffsetDateTime.of(
        calenderDate.get(Calendar.YEAR),
        calenderDate.get(Calendar.MONTH),
        calenderDate.get(Calendar.DATE),
        calenderDate.get(Calendar.HOUR),
        calenderDate.get(Calendar.MINUTE),
        calenderDate.get(Calendar.SECOND),
        calenderDate.get(Calendar.MILLISECOND),
        ZoneOffset.of("+09:00")
    )

}