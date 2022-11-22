package com.woory.data.source

import com.woory.data.model.GeoPointModel
import com.woory.data.model.PathModel

interface NetworkDataSource {

    suspend fun getAddressByPoint(geoPoint: GeoPointModel): Result<String>

    suspend fun getPath(start: GeoPointModel, dest: GeoPointModel): Result<PathModel>
}