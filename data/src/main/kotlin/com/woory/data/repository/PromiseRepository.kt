package com.woory.data.repository

import com.woory.data.model.AddedUserHpModel
import com.woory.data.model.GeoPointModel
import com.woory.data.model.LocationSearchModel
import com.woory.data.model.MagneticInfoModel
import com.woory.data.model.PromiseAlarmModel
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.PromiseModel
import com.woory.data.model.UserHpModel
import com.woory.data.model.UserLocationModel
import com.woory.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface PromiseRepository {

    suspend fun getAddressByPoint(geoPointModel: GeoPointModel): Result<String>

    suspend fun getPromiseByCode(promiseCode: String): Result<PromiseModel>

    suspend fun getPromiseByCodeAndListen(promiseCode: String): Flow<Result<PromiseModel>>

    suspend fun setPromise(promiseDataModel: PromiseDataModel): Result<String>

    suspend fun setUserLocation(userLocationModel: UserLocationModel): Result<Unit>

    suspend fun setUserHp(gameToken: String, userHpModel: UserHpModel): Result<Unit>

    suspend fun addPlayer(code: String, user: UserModel): Result<Unit>

    suspend fun getUserLocation(userId: String): Flow<Result<UserLocationModel>>

    suspend fun getUserHp(userId: String, gameToken: String): Flow<Result<UserHpModel>>

    suspend fun getPromiseAlarm(promiseCode: String): Result<PromiseAlarmModel>

    suspend fun getAllPromiseAlarms(): Result<List<PromiseAlarmModel>>

    suspend fun setPromiseAlarmByPromiseModel(promiseModel: PromiseModel): Result<Unit>

    suspend fun setPromiseAlarmByPromiseAlarmModel(promiseAlarmModel: PromiseAlarmModel): Result<Unit>

    suspend fun getSearchedLocationByKeyword(keyword: String): Result<List<LocationSearchModel>>

    suspend fun getJoinedPromiseList(): Result<List<PromiseAlarmModel>>

    suspend fun getMagneticInfoByCode(promiseCode: String): Result<MagneticInfoModel>

    suspend fun getMagneticInfoByCodeAndListen(promiseCode: String): Flow<Result<MagneticInfoModel>>

    suspend fun updateMagneticRadius(gameCode: String, radius: Double): Result<Unit>

    suspend fun decreaseMagneticRadius(gameCode: String, minusValue: Double): Result<Unit>

    suspend fun checkReEntryOfGame(gameCode: String, token: String): Result<Boolean>

    suspend fun sendOutUser(gameCode: String, token: String): Result<Unit>

    suspend fun setUserInitialHpData(gameCode: String, token: String): Result<Unit>

    suspend fun decreaseUserHp(gameCode: String, token: String): Result<Long>

    suspend fun getUserHpAndListen(gameCode: String, token: String): Flow<Result<AddedUserHpModel>>
}