package com.woory.firebase.mapper

import com.google.firebase.firestore.GeoPoint
import com.woory.data.model.GeoPointModel
import com.woory.data.model.UserLocationModel
import com.woory.firebase.model.UserLocationDocument

internal fun UserLocationDocument.asUserLocationModel() = UserLocationModel(
    id = this.id,
    location = GeoPointModel(this.location.latitude, this.location.longitude)
)

internal fun UserLocationModel.asUserLocation() = UserLocationDocument(
    id = this.id,
    location = GeoPoint(this.location.latitude, this.location.longitude)
)