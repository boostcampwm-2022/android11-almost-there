package com.woory.presentation.util

import com.woory.presentation.model.GeoPoint
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * @link https://shwjdqls.github.io/android-get-distance-between-locations/
 */
object DistanceUtil {
    private const val R = 6372.8 * 1000

    /**
     * @param geo1
     * @param geo2
     * @return 거리의 m값값
     * */
    fun getDistance(geo1: GeoPoint, geo2: GeoPoint): Double {
        val dLat = Math.toRadians(geo2.latitude - geo1.latitude)
        val dLon = Math.toRadians(geo2.longitude - geo1.longitude)
        val a =
            sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) *
                    cos(Math.toRadians(geo1.latitude)) * cos(Math.toRadians(geo2.latitude)
            )
        val c = 2 * asin(sqrt(a))
        return abs(R * c)
    }

}