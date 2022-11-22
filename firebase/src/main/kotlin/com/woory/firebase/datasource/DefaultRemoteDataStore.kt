package com.woory.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.woory.data.model.Promise
import com.woory.data.source.remote.RemoteDataStore
import com.woory.firebase.model.PromiseModel
import com.woory.firebase.model.mapper.asDomain
import kotlinx.coroutines.tasks.await

class DefaultRemoteDataStore : RemoteDataStore {

    private val store = Firebase.firestore

    override suspend fun fetchPromise(code: String): Promise? =
        store.collection(PROMISE_COLLECTION_PATH).document(code).get().await()
            .toObject(PromiseModel::class.java)?.asDomain()

    companion object {
        private const val PROMISE_COLLECTION_PATH = "promise"
    }
}