package com.woory.data.repository

import com.woory.data.model.GeoPointModel
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.UserStateModel
import com.woory.data.source.DatabaseDataSource
import com.woory.data.source.FirebaseDataSource
import com.woory.data.source.NetworkDataSource

class DefaultPromiseRepository(
    private val databaseDataSource: DatabaseDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val networkDataSource: NetworkDataSource
) : PromiseRepository {
    override fun createPromise(promise: PromiseDataModel): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getAddressByPoint(geoPoint: GeoPointModel): Result<String> {
        TODO("Not yet implemented")
    }

    override fun getPromiseByCode(code: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getUserState(id: String): Result<UserStateModel> {
        TODO("Not yet implemented")
    }

    override fun setUserState(userState: UserStateModel): Result<Unit> {
        TODO("Not yet implemented")
    }
}