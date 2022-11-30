package com.woory.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class MagneticInfoDocument(
    val centerPoint: GeoPoint = GeoPoint(37.5559, 126.9723),
    val radius: Double = 10.0,
    val timeStamp: Timestamp = Timestamp(1, 1)
)