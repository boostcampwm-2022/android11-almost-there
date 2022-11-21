package com.woory.data.repository

import com.woory.data.model.GeoPointModel
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.UserStateModel

interface PromiseRepository {

    fun getAddressByPoint(geoPoint: GeoPointModel): Result<String>

    fun getPromiseByCode(code: String): Result<Unit>

    fun createPromise(promise: PromiseDataModel): Result<Unit>

    fun setUserState(userState: UserStateModel): Result<Unit>

    fun getUserState(id: String): Result<UserStateModel>
}