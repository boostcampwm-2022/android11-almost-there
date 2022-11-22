package com.woory.network

import com.woory.data.model.PromiseDataModel
import com.woory.data.model.UserStateModel
import com.woory.data.source.FirebaseDataSource

class DefaultFirebaseDataSource() :
    FirebaseDataSource {
    override fun createPromise(promiseDataModel: PromiseDataModel): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getPromiseByCode(code: String): Result<PromiseDataModel> {
        TODO("Not yet implemented")
    }

    override fun getUserState(id: String): Result<UserStateModel> {
        TODO("Not yet implemented")
    }

    override fun setUserState(userState: UserStateModel): Result<Unit> {
        TODO("Not yet implemented")
    }
}