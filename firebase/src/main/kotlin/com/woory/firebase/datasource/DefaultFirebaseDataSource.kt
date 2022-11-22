package com.woory.firebase.datasource

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.UserHpModel
import com.woory.data.model.UserLocationModel
import com.woory.data.source.FirebaseDataSource
import com.woory.firebase.mapper.*
import com.woory.firebase.model.PromiseData
import com.woory.firebase.model.UserHp
import com.woory.firebase.model.UserLocation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DefaultFirebaseDataSource @Inject constructor(private val fireStore: FirebaseFirestore) : FirebaseDataSource {

    override fun getPromiseByCode(code: String): Result<PromiseDataModel> {
        val result = runCatching {
            val task = fireStore
                .collection("Promises")
                .document("Game1토큰")
                .get()
            Tasks.await(task)
            val res = task.result
                .toObject(PromiseData::class.java)
                ?.toPromiseDataModel() ?: throw IllegalStateException("Unmatched State with Server")
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

    override fun getUserLocationById(id: String): Flow<Result<UserLocationModel>> = callbackFlow {
        var documentReference: DocumentReference? = null

        kotlin.runCatching {
            documentReference = fireStore.collection("UserLocation").document(id)
        }.onFailure {
            trySend(Result.failure(it))
        }

        val subscription = documentReference?.addSnapshotListener { value, error ->
            if (value == null) {
                return@addSnapshotListener
            }

            kotlin.runCatching {
                val result = value.toObject(UserLocation::class.java)
                result?.let {
                    trySend(Result.success(it.toUserLocationModel()))
                } ?: throw IllegalStateException("DB의 데이터 값이 다릅니다.")
            }.onFailure {
                trySend(Result.failure(it))
            }
        }

        awaitClose { subscription?.remove() }
    }

    override fun setUserLocation(userLocationModel: UserLocationModel): Result<Unit> {
        val result = kotlin.runCatching {
            val res = fireStore
                .collection("UserLocation")
                .document(userLocationModel.id)
                .set(userLocationModel.toUserLocation())
        }

        return when (val exception = result.exceptionOrNull()) {
            null -> result
            else -> Result.failure(exception)
        }
    }

    override fun getUserHpById(id: String, gameToken: String): Flow<Result<UserHpModel>> = callbackFlow {
            var documentReference: DocumentReference? = null

            kotlin.runCatching {
                documentReference = fireStore
                    .collection("UserLocation")
                    .document(gameToken)
                    .collection("Hp")
                    .document(id)
            }.onFailure {
                trySend(Result.failure(it))
            }

            val subscription = documentReference?.addSnapshotListener { value, error ->
                if (value == null) {
                    return@addSnapshotListener
                }

                kotlin.runCatching {
                    val result = value.toObject(UserHp::class.java)
                    result?.let {
                        trySend(Result.success(it.toUserHpModel()))
                    } ?: throw IllegalStateException("DB의 데이터 값이 다릅니다.")
                }.onFailure {
                    trySend(Result.failure(it))
                }
            }

            awaitClose { subscription?.remove() }
        }

    override fun setUserHp(id: String, gameToken: String, newHp: Int): Result<Unit> {
        val result = kotlin.runCatching {
            val res = fireStore
                .collection("UserLocation")
                .document(gameToken)
                .collection("Hp")
                .document(id)
                .set(UserHp(id, newHp))
        }

        return when (val exception = result.exceptionOrNull()) {
            null -> result
            else -> Result.failure(exception)
        }
    }
}
