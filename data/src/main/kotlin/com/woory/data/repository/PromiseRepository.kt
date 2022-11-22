package com.woory.data.repository

import com.woory.data.model.*
import kotlinx.coroutines.flow.Flow

interface PromiseRepository {

    suspend fun getAddressByPoint(geoPoint: GeoPointModel): Result<String>

    suspend fun getPromiseByCode(code: String): Result<PromiseDataModel>

    suspend fun setPromise(promise: PromiseDataModel): Result<Unit>

    suspend fun setUserLocation(userLocation: UserLocationModel): Result<Unit>

    suspend fun setUserHp(gameToken: String, userHp: UserHpModel): Result<Unit>

    suspend fun getUserLocation(userId: String): Flow<Result<UserLocationModel>>

    suspend fun getUserHp(userId: String, gameToken: String): Flow<Result<UserHpModel>>

    @WorkerThread
    suspend fun fetchPromise(code: String): Result<Promise>
}