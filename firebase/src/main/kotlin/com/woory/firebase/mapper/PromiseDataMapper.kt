package com.woory.firebase.mapper

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.woory.data.model.*
import com.woory.firebase.model.Promise
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import java.util.*

internal fun Promise.toPromiseModel(): PromiseModel {

    val code = this.code

    val promiseLocation = LocationModel(
        geoPoint = GeoPointModel(this.destination.latitude, this.destination.longitude),
        address = this.address
    )

    val promiseDateTime = this.promiseTime.toDate().toOffsetDate()
    val gameDateTime = this.gameTime.toDate().toOffsetDate()

    val host = UserModel(
        id = this.host.userId,
        name = this.host.userName,
        image = UserImage(this.host.userImage.color, this.host.userImage.imageIdx)
    )

    val users = this.users.map { it.toUserModel() }

    return PromiseModel(
        code, PromiseDataModel(promiseLocation, promiseDateTime, gameDateTime, host, users)
    )
}

internal fun PromiseDataModel.toPromise(code: String): Promise {
    val address = this.promiseLocation.address
    val destination = GeoPoint(
        this.promiseLocation.geoPoint.latitude,
        this.promiseLocation.geoPoint.longitude
    )
    val gameTime = this.gameDateTime.toTimeStamp()
    val promiseTime = this.promiseDateTime.toTimeStamp()
    val host = this.host.toPromiseParticipant()
    val users = this.users.map { it.toPromiseParticipant() }

    return Promise(
        code = code,
        address = address,
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
