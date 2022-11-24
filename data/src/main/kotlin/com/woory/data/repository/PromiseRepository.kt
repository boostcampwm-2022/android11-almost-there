package com.woory.data.repository

import com.woory.data.model.GameTimeInfoModel
import com.woory.data.model.GeoPointModel
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.UserHpModel
import com.woory.data.model.UserLocationModel
import com.woory.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface PromiseRepository {

    suspend fun getAddressByPoint(geoPoint: GeoPointModel): Result<String>

    suspend fun getPromiseByCode(code: String): Result<PromiseDataModel>

    suspend fun setPromise(promise: PromiseDataModel): Result<Unit>

    suspend fun setUserLocation(userLocation: UserLocationModel): Result<Unit>

    suspend fun setUserHp(gameToken: String, userHp: UserHpModel): Result<Unit>

    suspend fun getUserLocation(userId: String): Flow<Result<UserLocationModel>>

    suspend fun getUserHp(userId: String, gameToken: String): Flow<Result<UserHpModel>>

    suspend fun getGameTimeByCode(code: String): Result<GameTimeInfoModel>

    suspend fun insertPromise(info: GameTimeInfoModel): Result<Unit>

    suspend fun addPlayer(code: String, user: UserModel): Result<Unit>
}