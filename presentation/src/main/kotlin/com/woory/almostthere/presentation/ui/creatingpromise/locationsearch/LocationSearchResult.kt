package com.woory.almostthere.presentation.ui.creatingpromise.locationsearch

import com.woory.almostthere.presentation.model.GeoPoint

data class LocationSearchResult(
    val name: String,
    val address: String,
    val location: GeoPoint
)