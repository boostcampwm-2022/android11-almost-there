package com.woory.almostthere.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeoPointModel(val latitude: Double, val longitude: Double) : Parcelable
