package com.woory.data.source

import com.woory.data.model.AddedUserHpModel
import com.woory.data.model.MagneticInfoModel
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.PromiseModel
import com.woory.data.model.UserLocationModel
import com.woory.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface FirebaseDataSource {

    suspend fun getPromiseByCode(code: String): Result<PromiseModel>

    suspend fun getPromiseByCodeAndListen(code: String): Flow<Result<PromiseModel>>

    suspend fun setPromise(promiseDataModel: PromiseDataModel): Result<String>

    suspend fun getUserLocationById(id: String): Flow<Result<UserLocationModel>>

    suspend fun setUserLocation(userLocationModel: UserLocationModel): Result<Unit>

    suspend fun setUserHp(gameToken: String, userHpModel: AddedUserHpModel): Result<Unit>

    suspend fun addPlayer(code: String, user: UserModel): Result<Unit>

    suspend fun getMagneticInfoByCodeAndListen(code: String): Flow<Result<MagneticInfoModel>>

    suspend fun updateMagneticRadius(gameCode: String, radius: Double): Result<Unit>

    suspend fun decreaseMagneticRadius(gameCode: String, minusValue: Double): Result<Unit>

    suspend fun getMagneticInfoByCode(code: String): Result<MagneticInfoModel>

    suspend fun checkReEntryOfGame(gameCode: String, token: String): Result<Boolean>

    suspend fun sendOutUser(gameCode: String, token: String): Result<Unit>

    suspend fun setUserInitialHpData(gameCode: String, token: String): Result<Unit>

    suspend fun decreaseUserHp(gameCode: String, token: String): Result<Long>

    suspend fun getUserHpAndListen(gameCode: String, token: String): Flow<Result<AddedUserHpModel>>

    suspend fun getUserHpList(gameCode: String): Result<List<AddedUserHpModel>>

    suspend fun getUserInfoList(gameCode: String): Result<List<UserModel>>

    suspend fun setPlayerArrived(gameCode: String, token: String): Result<Unit>

    suspend fun getPlayerArrived(gameCode: String, token: String): Flow<Result<Boolean>>

    suspend fun getGameRealtimeRanking(gameCode: String): Flow<Result<List<AddedUserHpModel>>>

    suspend fun setIsFinishedPromise(gameCode: String): Result<Unit>

    suspend fun getIsFinishedPromise(gameCode: String): Flow<Result<Boolean>>
}