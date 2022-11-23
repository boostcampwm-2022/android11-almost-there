package com.woory.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationModel(val geoPoint: GeoPointModel, val address: String) : Parcelable