package com.woory.data.repository

import com.woory.data.model.GeoPointModel
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.UserStateModel
import com.woory.data.source.DatabaseDataSource
import com.woory.data.source.FirebaseDataSource
import com.woory.data.source.NetworkDataSource
import javax.inject.Inject

class DefaultPromiseRepository @Inject constructor(
    private val databaseDataSource: DatabaseDataSource,
    private val firebaseDataSource: FirebaseDataSource,
    private val networkDataSource: NetworkDataSource
) : PromiseRepository {

    override suspend fun createPromise(promise: PromiseDataModel): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getAddressByPoint(geoPoint: GeoPointModel): Result<String> {
        TODO("Not yet implemented")
        // return networkDataSource.getAddressByPoint(geoPoint)
    }

    override suspend fun getPromiseByCode(code: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserState(id: String): Result<UserStateModel> {
        TODO("Not yet implemented")
    }

    override suspend fun setUserState(userState: UserStateModel): Result<Unit> {
        TODO("Not yet implemented")
    }
}