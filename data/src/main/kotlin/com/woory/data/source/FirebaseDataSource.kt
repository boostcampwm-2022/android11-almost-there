package com.woory.data.source

import com.woory.data.model.MagneticInfoModel
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.PromiseModel
import com.woory.data.model.UserHpModel
import com.woory.data.model.UserLocationModel
import com.woory.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface FirebaseDataSource {

    suspend fun getPromiseByCode(code: String): Result<PromiseModel>

    suspend fun getPromiseByCodeAndListen(code: String): Flow<Result<PromiseModel>>

    suspend fun setPromise(promiseDataModel: PromiseDataModel): Result<String>

    suspend fun getUserLocationById(id: String): Flow<Result<UserLocationModel>>

    suspend fun setUserLocation(userLocationModel: UserLocationModel): Result<Unit>

    suspend fun getUserHpById(id: String, gameToken: String): Flow<Result<UserHpModel>>

    suspend fun setUserHp(gameToken: String, userHpModel: UserHpModel): Result<Unit>

    suspend fun addPlayer(code: String, user: UserModel): Result<Unit>

    suspend fun getMagneticInfoByCodeAndListen(code: String): Flow<Result<MagneticInfoModel>>

    suspend fun updateMagneticRadius(gameCode: String, radius: Double): Result<Unit>

    suspend fun decreaseMagneticRadius(gameCode: String)

    suspend fun getMagneticInfoByCode(code: String): Result<MagneticInfoModel>
}