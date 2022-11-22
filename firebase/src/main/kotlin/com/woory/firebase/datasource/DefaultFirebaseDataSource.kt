package com.woory.firebase.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.UserStateModel
import com.woory.data.source.FirebaseDataSource
import com.woory.firebase.mapper.toPromiseData
import com.woory.firebase.mapper.toUserState
import com.woory.firebase.mapper.toUserStateModel
import com.woory.firebase.model.PromiseData
import com.woory.firebase.model.UserState
import javax.inject.Inject

class DefaultFirebaseDataSource @Inject constructor() : FirebaseDataSource {

    private val fireStore: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    override fun getPromiseByCode(code: String): Result<PromiseDataModel> {
        val result = runCatching {
            val res = fireStore
                .collection("Promises")
                .document("Game1토큰")
                .get().result
                .toObject(PromiseData::class.java)
                ?.toPromiseData() ?: throw IllegalStateException("Unmatched State with Server")
            res
        }

        return when (val exception = result.exceptionOrNull()) {
            null -> result
            else -> Result.failure(exception)
        }
    }

    override fun createPromise(promiseDataModel: PromiseDataModel): Result<Unit> {
        val result = kotlin.runCatching {
            val res = fireStore
                .collection("Promises")
                .document("Game1토큰")
                .set(promiseDataModel.toPromiseData())
        }

        return when (val exception = result.exceptionOrNull()) {
            null -> result
            else -> Result.failure(exception)
        }
    }

    override fun setUserState(userState: UserStateModel): Result<Unit> {
        val result = kotlin.runCatching {
            val res = fireStore
                .collection("UserLocation")
                .document(userState.id)
                .set(userState.toUserState())
        }
        return when (val exception = result.exceptionOrNull()) {
            null -> result
            else -> Result.failure(exception)
        }
    }

    override fun getUserState(id: String): Result<UserStateModel> {
        val result = kotlin.runCatching {
            val res = fireStore
                .collection("UserLocation")
                .document(id)
                .get().result.toObject(UserState::class.java)?.toUserStateModel()
                ?: throw IllegalStateException("Unmatched State with Server")
            res
        }

        return when (val exception = result.exceptionOrNull()) {
            null -> result
            else -> Result.failure(exception)
        }
    }
}
