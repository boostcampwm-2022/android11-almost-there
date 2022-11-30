package com.woory.network

import com.woory.data.model.GeoPointModel
import com.woory.data.model.LocationSearchModel
import com.woory.data.model.PathModel
import com.woory.data.model.RouteType
import com.woory.data.source.NetworkDataSource
import com.woory.network.model.mapper.asDomain
import org.json.JSONObject
import javax.inject.Inject

class DefaultNetworkDataSource @Inject constructor(
    private val tMapService: TMapService,
    private val oDSayService: ODsayService
) : NetworkDataSource {
    override suspend fun getAddressByPoint(geoPoint: GeoPointModel): Result<String> {
        return runCatching {
            tMapService.getReverseGeoCoding(
                lat = geoPoint.latitude.toString(),
                lon = geoPoint.longitude.toString()
            ).toString()
        }
    }

    override suspend fun getPublicTransitRoute(start: GeoPointModel, dest: GeoPointModel): Result<PathModel> {
        return runCatching {
            val response = oDSayService.getPublicTransitRoute(
                apiKey = BuildConfig.ODSAY_API_KEY,
                sx = start.longitude,
                sy = start.latitude,
                ex = dest.longitude,
                ey = dest.latitude,
            )

            val json = JSONObject(response.string()).getJSONObject("result")
            val searchType = json.getInt("searchType")

            val routeInfo = when (searchType) {
                IN_CITY -> {
                    json.getJSONArray("path")
                        .getJSONObject(0)
                        .getJSONObject("info")
                }
                OUT_CITY_DIRECT,
                OUT_CITY_TRANSFER -> {
                    json.getJSONArray("path")
                        .getJSONObject(0)
                }
                else -> throw IllegalArgumentException("odesay error")
            }

            val time = routeInfo.getInt("totalTime")
            val distance = routeInfo.getDouble("totalDistance")

            PathModel(
                routeType = RouteType.PUBLIC_TRANSIT,
                time = time,
                distance = distance
            )
        }
    }

    override suspend fun searchLocationByKeyword(keyword: String): Result<List<LocationSearchModel>> {
        return runCatching {
            tMapService.getSearchedLocation(
                version = 1,
                searchKeyword = keyword
            ).searchPoiInfo.pois.poi.map { it.asDomain() }
        }
    }

    companion object {
        const val IN_CITY = 0
        const val OUT_CITY_DIRECT = 1
        const val OUT_CITY_TRANSFER = 2
    }
}