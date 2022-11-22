package com.woory.firebase.mapper

import com.google.firebase.firestore.GeoPoint
import com.woory.data.model.GeoPointModel
import com.woory.data.model.UserLocationModel
import com.woory.firebase.model.UserLocation

internal fun UserLocation.toUserLocationModel() = UserLocationModel(
    id = this.Id,
    location = GeoPointModel(this.Location.latitude, this.Location.longitude)
)

internal fun UserLocationModel.toUserLocation() = UserLocation(
    Id = this.id,
    Location = GeoPoint(this.location.latitude, this.location.longitude)
)