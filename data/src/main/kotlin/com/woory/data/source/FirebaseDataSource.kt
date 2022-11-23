package com.woory.data.source

import com.woory.data.model.PromiseDataModel
import com.woory.data.model.PromiseModel
import com.woory.data.model.UserHpModel
import com.woory.data.model.UserLocationModel
import kotlinx.coroutines.flow.Flow

interface FirebaseDataSource {

    suspend fun getPromiseByCode(code: String): Result<PromiseModel>

    suspend fun setPromise(promiseDataModel: PromiseDataModel): Result<String>

    suspend fun getUserLocationById(id: String): Flow<Result<UserLocationModel>>

    suspend fun setUserLocation(userLocationModel: UserLocationModel): Result<Unit>

    suspend fun getUserHpById(id: String, gameToken: String): Flow<Result<UserHpModel>>

    suspend fun setUserHp(gameToken: String, userHpModel: UserHpModel): Result<Unit>
}