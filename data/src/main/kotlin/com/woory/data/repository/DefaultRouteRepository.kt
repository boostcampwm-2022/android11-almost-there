package com.woory.data.repository

import com.woory.data.model.GeoPointModel
import com.woory.data.model.PathModel
import com.woory.data.source.NetworkDataSource
import javax.inject.Inject

class DefaultRouteRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource
) : RouteRepository {
    override suspend fun getPath(start: GeoPointModel, dest: GeoPointModel): Result<PathModel> =
        networkDataSource.getPublicTransitRoute(start, dest)
}