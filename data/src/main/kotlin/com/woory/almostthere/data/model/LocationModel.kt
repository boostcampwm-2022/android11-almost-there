package com.woory.almostthere.data.model

data class LocationModel(val geoPoint: GeoPointModel, val address: String)

data class GeoPointModel(val latitude: Double, val longitude: Double)