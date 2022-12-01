package com.woory.firebase.mapper

import com.google.firebase.firestore.GeoPoint
import com.woory.data.model.GeoPointModel
import com.woory.data.model.LocationModel
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.PromiseModel
import com.woory.data.model.UserDataModel
import com.woory.data.model.UserModel
import com.woory.data.model.UserProfileImageModel
import com.woory.firebase.model.MagneticInfoDocument
import com.woory.firebase.model.PromiseDocument
import com.woory.firebase.util.TimeConverter.asOffsetDate
import com.woory.firebase.util.TimeConverter.asTimeStamp

internal fun PromiseDocument.asDomain(): PromiseModel {
    val promiseLocation = LocationModel(
        geoPoint = GeoPointModel(destination.latitude, destination.longitude),
        address = address
    )

    val promiseDateTime = promiseTime.asOffsetDate()
    val gameDateTime = gameTime.asOffsetDate()

    val host = UserModel(
        userId = host.userId,
        UserDataModel(
            name = host.userName,
            profileImage = UserProfileImageModel(host.userImage.color, host.userImage.imageIdx)
        )
    )

    val users = users.map { it.asUserModel() }

    return PromiseModel(
        code, PromiseDataModel(promiseLocation, promiseDateTime, gameDateTime, host, users)
    )
}

internal fun PromiseDataModel.asModel(code: String): PromiseDocument {
    val address = this.promiseLocation.address
    val destination = GeoPoint(
        this.promiseLocation.geoPoint.latitude,
        this.promiseLocation.geoPoint.longitude
    )
    val gameTime = gameDateTime.asTimeStamp()
    val promiseTime = promiseDateTime.asTimeStamp()
    val host = host.asPromiseParticipant()
    val users = users.map { it.asPromiseParticipant() }

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

fun PromiseDataModel.extractMagnetic(code: String): MagneticInfoDocument =
    MagneticInfoDocument(
        gameCode = code,
        centerPoint = GeoPoint(
            promiseLocation.geoPoint.latitude,
            promiseLocation.geoPoint.longitude
        )
    )
