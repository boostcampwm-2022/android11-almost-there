package com.woory.data.repository

import com.woory.data.model.GeoPointModel
import com.woory.data.model.PathModel

interface RouteRepository {

    suspend fun getMaximumVelocity(start: GeoPointModel, dest: GeoPointModel): Result<Double>

    suspend fun getMinimumTime(start: GeoPointModel, dest: GeoPointModel): Result<Int>

}