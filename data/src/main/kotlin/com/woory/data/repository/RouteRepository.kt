package com.woory.data.repository

import com.woory.data.model.GeoPointModel
import com.woory.data.model.PathModel

interface RouteRepository {

    suspend fun getPath(start: GeoPointModel, dest: GeoPointModel): Result<PathModel>

}