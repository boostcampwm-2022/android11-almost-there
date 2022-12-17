package com.woory.almostthere.presentation.model

import android.graphics.Color
import com.skt.tmap.overlay.TMapCircle

data class WooryTMapCircle(
    private val lat: Double,
    private val lon: Double,
    private val r: Double
) : TMapCircle(MAGNETIC_CIRCLE_KEY, lat, lon) {
    init {
        radius = r
        circleWidth = 2f
        areaAlpha = 10
        areaColor = Color.RED
        lineColor = Color.RED
    }

    companion object {
        private const val MAGNETIC_CIRCLE_KEY = "Magnetic"
    }
}