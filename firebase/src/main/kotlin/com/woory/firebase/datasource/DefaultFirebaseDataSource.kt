package com.woory.firebase.datasource

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.PromiseModel
import com.woory.data.model.UserHpModel
import com.woory.data.model.UserLocationModel
import com.woory.data.source.FirebaseDataSource
import com.woory.firebase.mapper.*
import com.woory.firebase.model.PromiseDocument
import com.woory.firebase.model.UserHpDocument
import com.woory.firebase.model.UserLocationDocument
import com.woory.firebase.util.InviteCodeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultFirebaseDataSource @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val scope: CoroutineScope
) : FirebaseDataSource {

    override suspend fun getPromiseByCode(code: String): Result<PromiseModel> {
        return withContext(scope.coroutineContext) {
            val result = runCatching {
                val task = fireStore
                    .collection("Promises")
                    .document(code)
                    .get()
                Tasks.await(task)
                val res = task.result
                    .toObject(PromiseDocument::class.java)
                    ?.toPromiseModel()
                    ?: throw IllegalStateException("Unmatched State with Server")
                res
            }

            // TODO: 현재 요청 시점이 해당 약속의 자기장 시작 시간보다 늦으면 new exception

            when (val exception = result.exceptionOrNull()) {
                null -> result
                else -> Result.failure(exception)
            }
        }
    }

    // TODO : 랜덤 Code 생성하는 로직 추가 (어디서 생성을 할지??)
    override suspend fun setPromise(promiseDataModel: PromiseDataModel): Result<String> {
        return withContext(scope.coroutineContext) {

            var generatedCode: String? = null
            var isDone = false
            while (isDone.not()) {
                generatedCode = InviteCodeUtil.getRandomInviteCode()
                fireStore
                    .collection("Promises")
                    .document(generatedCode)
                    .get()
                    .addOnSuccessListener {
                        if (it != null) {
                            isDone = true
                        }
                    }
            }
            requireNotNull(generatedCode)

            val result = kotlin.runCatching {
                fireStore
                    .collection("Promises")
                    .document(generatedCode)
                    .set(promiseDataModel.toPromise(generatedCode))
            }

            when (val exception = result.exceptionOrNull()) {
                null -> Result.success(generatedCode)
                else -> Result.failure(exception)
            }
        }
    }

    override suspend fun getUserLocationById(id: String): Flow<Result<UserLocationModel>> =
        callbackFlow {
            var documentReference: DocumentReference? = null

            kotlin.runCatching {
                documentReference = fireStore.collection("UserLocation").document(id)
            }.onFailure {
                trySend(Result.failure(it))
            }

            val subscription = documentReference?.addSnapshotListener { value, _ ->
                if (value == null) {
                    return@addSnapshotListener
                }

                kotlin.runCatching {
                    val result = value.toObject(UserLocationDocument::class.java)
                    result?.let {
                        trySend(Result.success(it.toUserLocationModel()))
                    } ?: throw IllegalStateException("DB의 데이터 값이 다릅니다.")
                }.onFailure {
                    trySend(Result.failure(it))
                }
            }

            awaitClose { subscription?.remove() }
        }

    override suspend fun setUserLocation(userLocationModel: UserLocationModel): Result<Unit> {
        return withContext(scope.coroutineContext) {
            val result = kotlin.runCatching {
                val res = fireStore
                    .collection("UserLocation")
                    .document(userLocationModel.id)
                    .set(userLocationModel.toUserLocation())
            }

            when (val exception = result.exceptionOrNull()) {
                null -> result
                else -> Result.failure(exception)
            }
        }
    }

    override suspend fun getUserHpById(id: String, gameToken: String): Flow<Result<UserHpModel>> =
        callbackFlow {
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
                    val result = value.toObject(UserHpDocument::class.java)
                    result?.let {
                        trySend(Result.success(it.toUserHpModel()))
                    } ?: throw IllegalStateException("DB의 데이터 값이 다릅니다.")
                }.onFailure {
                    trySend(Result.failure(it))
                }
            }

            awaitClose { subscription?.remove() }
        }

    override suspend fun setUserHp(gameToken: String, userHpModel: UserHpModel): Result<Unit> {
        return withContext(scope.coroutineContext) {
            val result = kotlin.runCatching {
                val res = fireStore
                    .collection("UserLocation")
                    .document(gameToken)
                    .collection("Hp")
                    .document(userHpModel.id)
                    .set(userHpModel.toUserHp())
            }

            when (val exception = result.exceptionOrNull()) {
                null -> result
                else -> Result.failure(exception)
            }
        }
    }
}
