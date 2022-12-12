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
            ).addressInfo.fullAddress
        }
    }

    override suspend fun getPublicTransitRoute(
        start: GeoPointModel,
        dest: GeoPointModel
    ): Result<PathModel> {
        return runCatching {
            val response = oDSayService.getPublicTransitRoute(
                apiKey = BuildConfig.ODSAY_API_KEY,
                sx = start.longitude,
                sy = start.latitude,
                ex = dest.longitude,
                ey = dest.latitude,
            )

            val json = JSONObject(response.string()).getJSONObject("result")
            val routeInfo = json.getJSONArray("path")
                .getJSONObject(0)
                .getJSONObject("info")

            val searchType = json.getInt("searchType")
            var time = routeInfo.getInt("totalTime")
            val distance = routeInfo.getDouble("totalDistance")

            if (searchType == 1) {
                val stationInfo = json.getJSONArray("path")
                    .getJSONObject(0)
                    .getJSONArray("subPath")

                val startStationInfo = stationInfo.getJSONObject(0)
                val ssy = startStationInfo.getDouble("startY")
                val ssx = startStationInfo.getDouble("startX")
                val stationStartTime = getPublicTransitRoute(
                    start = GeoPointModel(start.latitude, start.longitude),
                    dest = GeoPointModel(ssy, ssx)
                ).getOrThrow().time

                val endStationInfo = stationInfo.getJSONObject(stationInfo.length() - 1)
                val esy = endStationInfo.getDouble("endY")
                val esx = endStationInfo.getDouble("endX")
                val stationEndTime = getPublicTransitRoute(
                    start = GeoPointModel(dest.latitude, dest.longitude),
                    dest = GeoPointModel(esy, esx)
                ).getOrThrow().time
                time += stationStartTime + stationEndTime
            }

            PathModel(
                routeType = RouteType.PUBLIC_TRANSIT,
                time = time,
                distance = distance.toInt()
            )
        }
    }

    override suspend fun getCarRoute(
        start: GeoPointModel,
        dest: GeoPointModel
    ): Result<PathModel> {
        return runCatching {
            val response = tMapService.getCarRoute(
                startX = start.longitude,
                startY = start.latitude,
                endX = dest.longitude,
                endY = dest.latitude
            )

            val json = JSONObject(response.string()).getJSONArray("features")
                .getJSONObject(0)
                .getJSONObject("properties")

            val time = json.getInt("totalTime")
            val distance = json.getInt("totalDistance")

            PathModel(
                routeType = RouteType.CAR,
                time = time / 60,
                distance = distance
            )
        }
    }

    override suspend fun getWalkRoute(
        start: GeoPointModel,
        dest: GeoPointModel
    ): Result<PathModel> {
        return runCatching {
            val response = tMapService.getWalkRoute(
                startX = start.longitude,
                startY = start.latitude,
                endX = dest.longitude,
                endY = dest.latitude
            )

            val json = JSONObject(response.string()).getJSONArray("features")
                .getJSONObject(0)
                .getJSONObject("properties")

            val time = json.getInt("totalTime")
            val distance = json.getInt("totalDistance")

            PathModel(
                routeType = RouteType.WALK,
                time = time / 60,
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
}