package com.woory.data.repository

import com.woory.data.model.*
import kotlinx.coroutines.flow.Flow

interface PromiseRepository {

    suspend fun getAddressByPoint(geoPointModel: GeoPointModel): Result<String>

    suspend fun getPromiseByCode(promiseCode: String): Result<PromiseModel>

    suspend fun setPromise(promiseDataModel: PromiseDataModel): Result<String>

    suspend fun setUserLocation(userLocationModel: UserLocationModel): Result<Unit>

    suspend fun setUserHp(gameToken: String, userHpModel: UserHpModel): Result<Unit>

    suspend fun addPlayer(code: String, user: UserModel): Result<Unit>

    suspend fun getUserLocation(userId: String): Flow<Result<UserLocationModel>>

    suspend fun getUserHp(userId: String, gameToken: String): Flow<Result<UserHpModel>>

    suspend fun getPromiseAlarm(promiseCode: String): Result<PromiseAlarmModel>

    suspend fun setPromiseAlarmByPromiseModel(promiseModel: PromiseModel): Result<Unit>

    suspend fun setPromiseAlarmByPromiseAlarmModel(promiseAlarmModel: PromiseAlarmModel): Result<Unit>

    suspend fun getSearchedLocationByKeyword(keyword: String): Result<List<LocationSearchModel>>
}