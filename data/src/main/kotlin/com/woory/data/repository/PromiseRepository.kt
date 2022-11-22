package com.woory.data.repository

import androidx.annotation.WorkerThread
import com.woory.data.model.Promise

interface PromiseRepository {

    @WorkerThread
    suspend fun fetchPromise(code: String): Result<Promise>
}