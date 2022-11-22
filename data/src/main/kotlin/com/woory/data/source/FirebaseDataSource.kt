package com.woory.data.source

import com.woory.data.model.PromiseDataModel
import com.woory.data.model.UserHpModel
import com.woory.data.model.UserLocationModel
import kotlinx.coroutines.flow.Flow

interface FirebaseDataSource {

    fun getPromiseByCode(code: String): Result<PromiseDataModel>

    fun createPromise(promiseDataModel: PromiseDataModel): Result<Unit>

    fun getUserLocationById(id: String): Flow<Result<UserLocationModel>>

    fun setUserLocation(userLocationModel: UserLocationModel): Result<Unit>

    fun getUserHpById(id: String, gameToken: String): Flow<Result<UserHpModel>>

    fun setUserHp(id: String, gameToken: String, newHp: Int): Result<Unit>
}