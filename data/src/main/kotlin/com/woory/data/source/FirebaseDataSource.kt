package com.woory.data.source

import com.woory.data.model.PromiseDataModel
import com.woory.data.model.UserStateModel

interface FirebaseDataSource {

    fun getPromiseByCode(code: String): Result<PromiseDataModel>

    fun createPromise(promiseDataModel: PromiseDataModel): Result<Unit>

    fun setUserState(userState: UserStateModel): Result<Unit>

    fun getUserState(id: String): Result<UserStateModel>
}