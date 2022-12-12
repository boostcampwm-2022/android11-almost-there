package com.woory.firebase.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class MagneticInfoDocument(
    val centerPoint: GeoPoint = GeoPoint(37.5559, 126.9723),
    val gameCode: String = "",
    val radius: Double = 1.0,
    val initialRadius: Double = 0.0,
    val timeStamp: Timestamp = Timestamp(1, 1)
)