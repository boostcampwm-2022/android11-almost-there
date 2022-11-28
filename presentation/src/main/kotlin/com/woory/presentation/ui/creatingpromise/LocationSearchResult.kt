package com.woory.presentation.ui.creatingpromise

import com.woory.presentation.model.GeoPoint

data class LocationSearchResult(
    val name: String,
    val address: String,
    val location: GeoPoint
)