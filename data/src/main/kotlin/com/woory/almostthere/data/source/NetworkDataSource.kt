package com.woory.almostthere.data.source

import com.woory.almostthere.data.model.UserHpModel
import com.woory.almostthere.data.model.GeoPointModel
import com.woory.almostthere.data.model.LocationSearchModel
import com.woory.almostthere.data.model.MagneticInfoModel
import com.woory.almostthere.data.model.PathModel
import com.woory.almostthere.data.model.PromiseDataModel
import com.woory.almostthere.data.model.PromiseHistoryModel
import com.woory.almostthere.data.model.PromiseModel
import com.woory.almostthere.data.model.UserLocationModel
import com.woory.almostthere.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface NetworkDataSource {

    suspend fun getAddressByPoint(geoPoint: GeoPointModel): Result<String>

    suspend fun getPublicTransitRoute(start: GeoPointModel, dest: GeoPointModel): Result<PathModel>

    suspend fun getCarRoute(start: GeoPointModel, dest: GeoPointModel): Result<PathModel>

    suspend fun getWalkRoute(start: GeoPointModel, dest: GeoPointModel): Result<PathModel>

    suspend fun searchLocationByKeyword(keyword: String): Result<List<LocationSearchModel>>

    suspend fun getPromiseByCode(code: String): Result<PromiseModel>

    suspend fun getPromiseByCodeAndListen(code: String): Flow<Result<PromiseModel>>

    suspend fun setPromise(promiseDataModel: PromiseDataModel): Result<String>

    suspend fun getUserLocationById(id: String): Flow<Result<UserLocationModel>>

    suspend fun setUserLocation(userLocationModel: UserLocationModel): Result<Unit>

    suspend fun addPlayer(code: String, user: UserModel): Result<Unit>

    suspend fun getMagneticInfoByCodeAndListen(code: String): Flow<Result<MagneticInfoModel>>

    suspend fun updateMagneticRadius(gameCode: String, radius: Double): Result<Unit>

    suspend fun updateInitialMagneticRadius(gameCode: String): Result<Unit>

    suspend fun decreaseMagneticRadius(gameCode: String, minusValue: Double): Result<Unit>

    suspend fun getMagneticInfoByCode(code: String): Result<MagneticInfoModel>

    suspend fun checkReEntryOfGame(gameCode: String, token: String): Result<Boolean>

    suspend fun sendOutUser(gameCode: String, token: String): Result<Unit>

    suspend fun setUserInitialHpData(gameCode: String, token: String): Result<Int>

    suspend fun decreaseUserHp(gameCode: String, token: String, newHp: Int): Result<Int>

    suspend fun getUserHpAndListen(gameCode: String, token: String): Flow<Result<UserHpModel>>

    suspend fun getUserHpList(gameCode: String): Result<List<UserHpModel>>

    suspend fun getUserInfoList(gameCode: String): Result<List<UserModel>>

    suspend fun setPlayerArrived(gameCode: String, token: String): Result<Unit>

    suspend fun getPlayerArrived(gameCode: String, token: String): Flow<Result<Boolean>>

    suspend fun setIsFinishedPromise(gameCode: String): Result<Unit>

    suspend fun getIsFinishedPromise(gameCode: String): Flow<Result<Boolean>>

    suspend fun setIsStartedGame(gameCode: String): Result<Unit>

    suspend fun getIsStartedGame(gameCode: String): Flow<Result<Boolean>>

    suspend fun setUserReady(gameCode: String, token: String): Result<Unit>

    suspend fun getIsReadyUser(gameCode: String, token: String): Flow<Result<Boolean>>

    suspend fun getReadyUsers(gameCode: String): Flow<Result<List<String>>>

    suspend fun getReadyUserList(code: String): Result<List<UserModel>>

    suspend fun getPromisesByCodes(codes: List<String>): Flow<List<PromiseHistoryModel>?>
}