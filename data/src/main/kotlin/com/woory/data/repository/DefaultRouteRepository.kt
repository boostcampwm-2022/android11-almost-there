package com.woory.data.repository

import android.util.Log
import com.woory.data.model.GeoPointModel
import com.woory.data.model.PathModel
import com.woory.data.model.RouteType
import com.woory.data.source.NetworkDataSource
import javax.inject.Inject

class DefaultRouteRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : RouteRepository {
    override suspend fun getMaximumVelocity(start: GeoPointModel, dest: GeoPointModel): Result<Double> {
        return runCatching {
            val defaultPathModel = PathModel(RouteType.NONE, 0, Int.MAX_VALUE)

            with(networkDataSource) {
                val publicTransitPathModel = getPublicTransitRoute(start, dest).getOrDefault(defaultPathModel)
                val carPathModel = getCarRoute(start, dest).getOrDefault(defaultPathModel)
                val walkPathModel = getWalkRoute(start, dest).getOrDefault(defaultPathModel)

                arrayOf(publicTransitPathModel.velocity, carPathModel.velocity, walkPathModel.velocity).max()
            }
        }
    }
}