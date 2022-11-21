package com.woory.data.source

import com.woory.data.model.GeoPointModel
import com.woory.data.model.PathModel

interface NetworkDataSource {

    fun getAddressByPoint(geoPoint: GeoPointModel): Result<String>

    fun getPath(start: GeoPointModel, dest: GeoPointModel): Result<PathModel>
}