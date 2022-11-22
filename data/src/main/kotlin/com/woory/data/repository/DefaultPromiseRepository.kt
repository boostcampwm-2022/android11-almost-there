package com.woory.data.repository

import androidx.annotation.WorkerThread
import com.woory.data.model.Promise
import com.woory.data.source.remote.RemoteDataStore
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DefaultPromiseRepository @Inject constructor(
    private val remoteDataStore: RemoteDataStore,
    private val ioDispatcher: CoroutineDispatcher
) : PromiseRepository {

    @WorkerThread
    override suspend fun fetchPromise(code: String): Result<Promise> = with(ioDispatcher) {
        runCatching {
            remoteDataStore.fetchPromise(code) ?: throw NoSuchElementException()
        }
    }
}