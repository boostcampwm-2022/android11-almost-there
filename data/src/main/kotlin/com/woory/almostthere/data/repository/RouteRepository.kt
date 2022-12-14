package com.woory.almostthere.data.repository

import com.woory.almostthere.data.model.GeoPointModel

interface RouteRepository {

    suspend fun getMaximumVelocity(start: GeoPointModel, dest: GeoPointModel): Result<Double>

    suspend fun getMinimumTime(start: GeoPointModel, dest: GeoPointModel): Result<Int>
}