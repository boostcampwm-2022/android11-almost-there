package com.woory.firebase.mapper

import com.google.firebase.firestore.GeoPoint
import com.woory.data.model.GeoPointModel
import com.woory.data.model.LocationModel
import com.woory.data.model.UserStateModel
import com.woory.firebase.model.UserState

internal fun UserStateModel.toUserState(): UserState = UserState(
    this.id,
    this.hp,
    GeoPoint(this.location.geoPoint.latitude, this.location.geoPoint.longitude),
    this.location.address
)

internal fun UserState.toUserStateModel(): UserStateModel{
    val location = LocationModel(
        GeoPointModel(this.location.latitude, this.location.longitude),
        this.address
    )

    return UserStateModel(
        this.id,
        this.hp,
        location
    )
}