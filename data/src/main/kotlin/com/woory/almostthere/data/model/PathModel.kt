package com.woory.almostthere.data.model

data class PathModel(
    val routeType: RouteType,
    val distance: Int,
    val time: Int,
) {
    val velocity = distance.toDouble() / time
}