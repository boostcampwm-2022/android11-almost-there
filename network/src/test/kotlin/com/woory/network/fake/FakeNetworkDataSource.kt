package com.woory.network.fake

import com.woory.data.model.GeoPointModel
import com.woory.data.model.LocationSearchModel
import com.woory.data.model.PathModel
import com.woory.data.source.NetworkDataSource
import com.woory.network.TMapService

class FakeNetworkDataSource(private val service: TMapService): NetworkDataSource {

    override suspend fun getAddressByPoint(geoPoint: GeoPointModel): Result<String> {
        return runCatching {
            service.getReverseGeoCoding(lat = geoPoint.latitude.toString(), lon = geoPoint.longitude.toString()).toString()
        }
    }

    override suspend fun getCarRoute(start: GeoPointModel, dest: GeoPointModel): Result<PathModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getPublicTransitRoute(
        start: GeoPointModel,
        dest: GeoPointModel
    ): Result<PathModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getWalkRoute(
        start: GeoPointModel,
        dest: GeoPointModel
    ): Result<PathModel> {
        TODO("Not yet implemented")
    }

    override suspend fun searchLocationByKeyword(keyword: String): Result<List<LocationSearchModel>> {
        TODO("Not yet implemented")
    }
}