package com.woory.firebase.datasource

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.woory.data.model.MagneticInfoModel
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.PromiseModel
import com.woory.data.model.UserHpModel
import com.woory.data.model.UserLocationModel
import com.woory.data.model.UserModel
import com.woory.data.source.FirebaseDataSource
import com.woory.firebase.mapper.asDomain
import com.woory.firebase.mapper.asModel
import com.woory.firebase.mapper.asPromiseParticipant
import com.woory.firebase.mapper.extractMagnetic
import com.woory.firebase.model.MagneticInfoDocument
import com.woory.firebase.model.PromiseDocument
import com.woory.firebase.model.UserHpDocument
import com.woory.firebase.model.UserLocationDocument
import com.woory.firebase.util.InviteCodeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultFirebaseDataSource @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val scope: CoroutineScope
) : FirebaseDataSource {

    override suspend fun getPromiseByCode(code: String): Result<PromiseModel> =
        withContext(scope.coroutineContext) {
            val result = runCatching {
                val task = fireStore
                    .collection(PROMISE_COLLECTION_NAME)
                    .document(code)
                    .get()
                Tasks.await(task)
                val res = task.result
                    .toObject(PromiseDocument::class.java)
                    ?.asDomain()
                    ?: throw IllegalStateException("Unmatched State with Server")
                res
            }

            // TODO: 현재 요청 시점이 해당 약속의 자기장 시작 시간보다 늦으면 new exception

            when (val exception = result.exceptionOrNull()) {
                null -> result
                else -> Result.failure(exception)
            }
        }

    override suspend fun getPromiseByCodeAndListen(code: String): Flow<Result<PromiseModel>> =
        callbackFlow {
            var documentReference: DocumentReference? = null

            runCatching {
                documentReference = fireStore.collection(PROMISE_COLLECTION_NAME).document(code)
            }.onFailure {
                trySend(Result.failure(it))
            }

            val subscription = documentReference?.addSnapshotListener { value, _ ->
                if (value == null) {
                    return@addSnapshotListener
                }

                runCatching {
                    val result = value.toObject(PromiseDocument::class.java)
                    result?.let {
                        trySend(Result.success(it.asDomain()))
                    } ?: throw IllegalStateException("Unmatched State with Server")
                }.onFailure {
                    trySend(Result.failure(it))
                }
            }

            awaitClose { subscription?.remove() }
        }

    override suspend fun setPromise(promiseDataModel: PromiseDataModel): Result<String> =
        withContext(scope.coroutineContext) {
            var generatedCode: String? = null
            var isDone = false
            while (isDone.not()) {
                generatedCode = InviteCodeUtil.getRandomInviteCode()
                val task = fireStore
                    .collection(PROMISE_COLLECTION_NAME)
                    .document(generatedCode)
                    .get()
                Tasks.await(task)
                if (task.result.data == null) {
                    isDone = true
                }
            }
            requireNotNull(generatedCode)

            val result = runCatching {
                fireStore
                    .collection(PROMISE_COLLECTION_NAME)
                    .document(generatedCode).apply {
                        set(promiseDataModel.asModel(generatedCode))
                        collection(MAGNETIC_COLLECTION_NAME)
                            .document(generatedCode)
                            .set(promiseDataModel.extractMagnetic())
                    }
            }

            when (val exception = result.exceptionOrNull()) {
                null -> Result.success(generatedCode)
                else -> Result.failure(exception)
            }
        }

    override suspend fun getUserLocationById(id: String): Flow<Result<UserLocationModel>> =
        callbackFlow {
            var documentReference: DocumentReference? = null

            runCatching {
                documentReference = fireStore.collection(LOCATION_COLLECTION_NAME).document(id)
            }.onFailure {
                trySend(Result.failure(it))
            }

            val subscription = documentReference?.addSnapshotListener { value, _ ->
                if (value == null) {
                    return@addSnapshotListener
                }

                runCatching {
                    val result = value.toObject(UserLocationDocument::class.java)
                    result?.let {
                        trySend(Result.success(it.asDomain()))
                    } ?: throw IllegalStateException("DB의 데이터 값이 다릅니다.")
                }.onFailure {
                    trySend(Result.failure(it))
                }
            }

            awaitClose { subscription?.remove() }
        }

    override suspend fun setUserLocation(userLocationModel: UserLocationModel): Result<Unit> =
        withContext(scope.coroutineContext) {
            val result = runCatching {
                val res = fireStore
                    .collection(LOCATION_COLLECTION_NAME)
                    .document(userLocationModel.id)
                    .set(userLocationModel.asModel())
            }

            when (val exception = result.exceptionOrNull()) {
                null -> result
                else -> Result.failure(exception)
            }
        }

    override suspend fun getUserHpById(id: String, gameToken: String): Flow<Result<UserHpModel>> =
        callbackFlow {
            var documentReference: DocumentReference? = null

            runCatching {
                documentReference = fireStore
                    .collection(LOCATION_COLLECTION_NAME)
                    .document(gameToken)
                    .collection(HP_COLLECTION_NAME)
                    .document(id)
            }.onFailure {
                trySend(Result.failure(it))
            }

            val subscription = documentReference?.addSnapshotListener { value, error ->
                if (value == null) {
                    return@addSnapshotListener
                }

                runCatching {
                    val result = value.toObject(UserHpDocument::class.java)
                    result?.let {
                        trySend(Result.success(it.asDomain()))
                    } ?: throw IllegalStateException("DB의 데이터 값이 다릅니다.")
                }.onFailure {
                    trySend(Result.failure(it))
                }
            }

            awaitClose { subscription?.remove() }
        }

    override suspend fun setUserHp(gameToken: String, userHpModel: UserHpModel): Result<Unit> =
        withContext(scope.coroutineContext) {
            val result = runCatching {
                val res = fireStore
                    .collection(LOCATION_COLLECTION_NAME)
                    .document(gameToken)
                    .collection(HP_COLLECTION_NAME)
                    .document(userHpModel.id)
                    .set(userHpModel.asModel())
            }

            when (val exception = result.exceptionOrNull()) {
                null -> result
                else -> Result.failure(exception)
            }
        }

    override suspend fun addPlayer(code: String, user: UserModel): Result<Unit> =
        withContext(scope.coroutineContext) {
            val result = runCatching {
                val res = fireStore
                    .collection(PROMISE_COLLECTION_NAME)
                    .document(code)
                    .update("users", FieldValue.arrayUnion(user.asPromiseParticipant()))
                    .await()
            }

            when (val exception = result.exceptionOrNull()) {
                null -> result
                else -> Result.failure(exception)
            }
        }

    /**
     * 자기장 반지름과 중심 좌표를 가져오는 함수
     */
    override suspend fun getMagneticInfoByCode(code: String): Result<MagneticInfoModel> =
        withContext(scope.coroutineContext) {
            val result = runCatching {
                val task = fireStore
                    .collection(PROMISE_COLLECTION_NAME)
                    .document(code)
                    .collection(MAGNETIC_COLLECTION_NAME)
                    .document(code)
                    .get()
                Tasks.await(task)
                val res = task
                    .result
                    .toObject(MagneticInfoDocument::class.java)
                    ?.asDomain()
                    ?: throw IllegalStateException("Unmatched State with Server")

                res
            }

            when (val exception = result.exceptionOrNull()) {
                null -> result
                else -> Result.failure(exception)
            }
        }

    /**
     * 자기장 반지름을 업데이트하는 함수
     */
    override suspend fun updateMagneticRadius(gameCode: String, radius: Float): Result<Unit> =
        withContext(scope.coroutineContext) {
            val result = kotlin.runCatching {
                val reference = fireStore
                    .collection(PROMISE_COLLECTION_NAME)
                    .document(gameCode)
                    .collection(MAGNETIC_COLLECTION_NAME)
                    .document(gameCode)

                fireStore.runTransaction { transaction ->
                    val snapshot = transaction.get(reference)
                    val serverRadius = snapshot.getLong(RADIUS_KEY) ?: return@runTransaction

                    val maxValue = maxOf(serverRadius, radius.toLong())
                    transaction.update(reference, mapOf(RADIUS_KEY to maxValue))
                }.addOnFailureListener {
                    throw it
                }
            }
            when (val exception = result.exceptionOrNull()) {
                null -> Result.success(Unit)
                else -> Result.failure(exception)
            }
        }


    companion object {
        private const val PROMISE_COLLECTION_NAME = "Promises"
        private const val LOCATION_COLLECTION_NAME = "UserLocation"
        private const val MAGNETIC_COLLECTION_NAME = "Magnetic"
        private const val HP_COLLECTION_NAME = "Hp"
        private const val RADIUS_KEY = "radius"
    }
}
