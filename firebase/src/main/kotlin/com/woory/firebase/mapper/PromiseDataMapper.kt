package com.woory.firebase.mapper

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.woory.data.model.*
import com.woory.firebase.model.PromiseData
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import java.util.*

internal fun PromiseData.toPromiseDataModel(): PromiseDataModel {

    val code = this.code

    val promiseLocation = LocationModel(
        geoPoint = GeoPointModel(this.destination.latitude, this.destination.longitude),
        address = this.address
    )

    val promiseDateTime = this.promiseTime.toDate().toOffsetDate()
    val gameDateTime = this.gameTime.toDate().toOffsetDate()

    val host = UserModel(
        userId = this.host.userId,
        userName = this.host.userName,
        userImage = UserImage(this.host.userImage.color, this.host.userImage.imageIdx)
    )

    val users = this.users.map { it.toUserModel() }

    return PromiseDataModel(
        code, promiseLocation, promiseDateTime, gameDateTime, host, users
    )
}

internal fun PromiseDataModel.toPromiseData(): PromiseData {
    val address = this.promiseLocation.address
    val code = this.code
    val destination = GeoPoint(
        this.promiseLocation.geoPoint.latitude,
        this.promiseLocation.geoPoint.longitude
    )
    val gameTime = this.gameDateTime.toTimeStamp()
    val promiseTime = this.promiseDateTime.toTimeStamp()
    val host = this.host.toPromiseParticipant()
    val users = this.users.map { it.toPromiseParticipant() }

    return PromiseData(
        address = address,
        code = code,
        destination = destination,
        host = host,
        gameTime = gameTime,
        promiseTime = promiseTime,
        users = users
    )
}

private fun OffsetDateTime.toTimeStamp(): Timestamp =
    Timestamp(Date(this.toInstant().toEpochMilli()))


private fun Date.toOffsetDate(): OffsetDateTime {
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
