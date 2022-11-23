package com.woory.data.repository

import com.woory.data.model.*
import com.woory.data.source.DatabaseDataSource
import com.woory.data.source.FirebaseDataSource
import com.woory.data.source.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultPromiseRepository @Inject constructor(
    private val databaseDataSource: DatabaseDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val networkDataSource: NetworkDataSource
) : PromiseRepository {

    override suspend fun setPromise(promise: PromiseDataModel): Result<Unit> =
        firebaseDataSource.setPromise(promise)

    override suspend fun getAddressByPoint(geoPoint: GeoPointModel): Result<String> =
        networkDataSource.getAddressByPoint(geoPoint)

    override suspend fun getPromiseByCode(code: String): Result<PromiseDataModel> =
        firebaseDataSource.getPromiseByCode(code)

    override suspend fun setUserLocation(userLocation: UserLocationModel): Result<Unit> =
        firebaseDataSource.setUserLocation(userLocation)

    override suspend fun setUserHp(gameToken: String, userHp: UserHpModel): Result<Unit> =
        firebaseDataSource.setUserHp(gameToken, userHp)

    override suspend fun getUserLocation(userId: String): Flow<Result<UserLocationModel>> =
        firebaseDataSource.getUserLocationById(userId)

    override suspend fun getUserHp(userId: String, gameToken: String): Flow<Result<UserHpModel>> =
        firebaseDataSource.getUserHpById(userId, gameToken)

    @WorkerThread
    override suspend fun fetchPromise(code: String): Result<Promise> = with(ioDispatcher) {
        runCatching {
            remoteDataStore.fetchPromise(code) ?: throw NoSuchElementException()
        }
    }
}