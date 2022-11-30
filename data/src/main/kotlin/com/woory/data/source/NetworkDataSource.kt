package com.woory.data.source

import com.woory.data.model.GeoPointModel
import com.woory.data.model.LocationSearchModel
import com.woory.data.model.PathModel

interface NetworkDataSource {

    suspend fun getAddressByPoint(geoPoint: GeoPointModel): Result<String>

    suspend fun getPublicTransitRoute(start: GeoPointModel, dest: GeoPointModel): Result<PathModel>

    suspend fun searchLocationByKeyword(keyword: String): Result<List<LocationSearchModel>>
}