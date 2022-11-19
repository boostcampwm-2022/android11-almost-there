package com.woory.almostthere.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationModel(val latitude: Double, val longitude: Double, val location: String): Parcelable