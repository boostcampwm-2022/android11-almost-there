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

    override suspend fun setPromise(promise: PromiseDataModel): Result<String> =
        firebaseDataSource.setPromise(promise)

    override suspend fun getAddressByPoint(geoPoint: GeoPointModel): Result<String> =
        networkDataSource.getAddressByPoint(geoPoint)

    override suspend fun getPromiseByCode(code: String): Result<PromiseModel> =
        firebaseDataSource.getPromiseByCode(code)

    override suspend fun setUserLocation(userLocation: UserLocationModel): Result<Unit> =
        firebaseDataSource.setUserLocation(userLocation)

    override suspend fun setUserHp(gameToken: String, userHp: UserHpModel): Result<Unit> =
        firebaseDataSource.setUserHp(gameToken, userHp)

    override suspend fun getUserLocation(userId: String): Flow<Result<UserLocationModel>> =
        firebaseDataSource.getUserLocationById(userId)

    override suspend fun getUserHp(userId: String, gameToken: String): Flow<Result<UserHpModel>> =
        firebaseDataSource.getUserHpById(userId, gameToken)

    override suspend fun getGameTimeByCode(code: String): Result<GameTimeInfoModel> =
        databaseDataSource.getGameTimeByCode(code)

    override suspend fun insertPromise(info: GameTimeInfoModel): Result<Unit> =
        databaseDataSource.insertGameTime(info)

    override suspend fun addPlayer(code: String, user: UserModel): Result<Unit> =
        firebaseDataSource.addPlayer(code, user)
}