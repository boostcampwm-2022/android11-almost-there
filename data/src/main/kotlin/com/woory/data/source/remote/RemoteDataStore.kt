package com.woory.data.source.remote

import com.woory.data.model.Promise

interface RemoteDataStore {

    suspend fun fetchPromise(code: String): Promise?
}