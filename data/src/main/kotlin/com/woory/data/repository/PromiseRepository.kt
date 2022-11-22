package com.woory.data.repository

import com.woory.data.model.GeoPointModel
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.UserStateModel

interface PromiseRepository {

    suspend fun getAddressByPoint(geoPoint: GeoPointModel): Result<String>

    suspend fun getPromiseByCode(code: String): Result<Unit>

    suspend fun createPromise(promise: PromiseDataModel): Result<Unit>

    suspend fun setUserState(userState: UserStateModel): Result<Unit>

    suspend fun getUserState(id: String): Result<UserStateModel>
}