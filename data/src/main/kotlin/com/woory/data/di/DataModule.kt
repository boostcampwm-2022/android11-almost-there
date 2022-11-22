package com.woory.data.di

import com.woory.data.repository.DefaultPromiseRepository
import com.woory.data.repository.PromiseRepository
import com.woory.data.source.remote.RemoteDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Provides
    @Singleton
    fun providePromiseRepository(
        remoteDataStore: RemoteDataStore,
        ioDispatcher: CoroutineDispatcher
    ): PromiseRepository =
        DefaultPromiseRepository(remoteDataStore, ioDispatcher)
}