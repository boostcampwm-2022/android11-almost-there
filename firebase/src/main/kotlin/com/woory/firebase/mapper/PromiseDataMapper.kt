package com.woory.firebase.mapper

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.woory.data.model.GeoPointModel
import com.woory.data.model.LocationModel
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.UserModel
import com.woory.firebase.model.PromiseData
import com.woory.firebase.model.PromiseParticipant
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import java.util.*

internal fun PromiseData.toPromiseData(): PromiseDataModel {
    val promiseLocation = LocationModel(
        geoPoint = GeoPointModel(this.Destination.latitude, this.Destination.longitude),
        address = this.Address
    )

    val promiseDateTime = this.PromiseTime.toDate().toOffsetDate()
    val gameDateTime = this.GameTime.toDate().toOffsetDate()

    val host = UserModel(
        name = this.Host["HostName"].toString(),
        image = ""
    )

    val users = listOf(host)

    return PromiseDataModel(
        promiseLocation, promiseDateTime, gameDateTime, host, users
    )
}

internal fun PromiseDataModel.toPromiseData(): PromiseData {
    val destination = GeoPoint(
        this.promiseLocation.geoPoint.latitude,
        this.promiseLocation.geoPoint.longitude
    )
    val address = this.promiseLocation.address
    val gameTime = this.gameDateTime.toTimeStamp()
    val promiseTime = this.promiseDateTime.toTimeStamp()
    val host = mapOf(this.host.name to this.host.image)
    val users = listOf(PromiseParticipant(mapOf(), this.host.name, this.host.image))

    return PromiseData(
        destination, address, gameTime, promiseTime, host, users
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
