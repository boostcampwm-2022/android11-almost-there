package com.woory.almostthere.data.repository

import com.woory.almostthere.data.model.GeoPointModel
import com.woory.almostthere.data.model.PathModel
import com.woory.almostthere.data.model.RouteType
import com.woory.almostthere.data.source.NetworkDataSource
import javax.inject.Inject

class DefaultRouteRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : RouteRepository {
    override suspend fun getMaximumVelocity(
        start: GeoPointModel,
        dest: GeoPointModel
    ): Result<Double> {
        return runCatching {
            val defaultPathModel = PathModel(RouteType.NONE, 0, Int.MAX_VALUE)

            with(networkDataSource) {
                arrayOf(
                    getPublicTransitRoute(start, dest),
                    getCarRoute(start, dest),
                    getWalkRoute(start, dest)
                ).map { it.getOrDefault(defaultPathModel) }
                    .filter { it.time != 0 }
                    .map { it.velocity }
                    .max()
            }
        }
    }

    override suspend fun getMinimumTime(
        start: GeoPointModel,
        dest: GeoPointModel
    ): Result<Int> {
        return runCatching {
            val defaultPathModel = PathModel(RouteType.NONE, 0, 0)

            with(networkDataSource) {
                arrayOf(
                    getPublicTransitRoute(start, dest),
                    getCarRoute(start, dest),
                    getWalkRoute(start, dest)
                ).map { it.getOrDefault(defaultPathModel) }
                    .filter { it.time != 0 }.minOfOrNull { it.time } ?: -1
            }
        }
    }
}