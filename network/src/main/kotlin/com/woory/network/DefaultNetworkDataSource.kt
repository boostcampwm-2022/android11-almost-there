package com.woory.network

import com.woory.data.model.GeoPointModel
import com.woory.data.model.LocationSearchModel
import com.woory.data.model.PathModel
import com.woory.data.source.NetworkDataSource
import com.woory.network.model.mapper.asDomain
import javax.inject.Inject

class DefaultNetworkDataSource @Inject constructor(private val service: TMapService) :
    NetworkDataSource {
    override suspend fun getAddressByPoint(geoPoint: GeoPointModel): Result<String> {
        return runCatching {
            service.getReverseGeoCoding(
                lat = geoPoint.latitude.toString(),
                lon = geoPoint.longitude.toString()
            ).addressInfo.fullAddress
        }
    }

    override suspend fun getPath(start: GeoPointModel, dest: GeoPointModel): Result<PathModel> {
        TODO("Not yet implemented")
    }

    override suspend fun searchLocationByKeyword(keyword: String): Result<List<LocationSearchModel>> {
        return runCatching {
            service.getSearchedLocation(
                version = 1,
                searchKeyword = keyword
            ).searchPoiInfo.pois.poi.map { it.asDomain() }
        }
    }
}