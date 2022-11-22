package com.woory.firebase.mapper

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.woory.data.model.*
import com.woory.firebase.model.PromiseData
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import java.util.*

internal fun PromiseData.toPromiseDataModel(): PromiseDataModel {

    val code = this.Code

    val promiseLocation = LocationModel(
        geoPoint = GeoPointModel(this.Destination.latitude, this.Destination.longitude),
        address = this.Address
    )

    val promiseDateTime = this.PromiseTime.toDate().toOffsetDate()
    val gameDateTime = this.GameTime.toDate().toOffsetDate()

    val host = UserModel(
        id = this.Host.UserId,
        name = this.Host.UserName,
        image = UserImage(this.Host.UserImage.Color, this.Host.UserImage.ImageIdx)
    )

    val users = this.Users.map { it.toUserModel() }

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
        Address = address,
        Code = code,
        Destination = destination,
        Host = host,
        GameTime = gameTime,
        PromiseTime = promiseTime,
        Users = users
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
