package com.woory.data.di

import com.woory.data.repository.DefaultPromiseRepository
import com.woory.data.repository.PromiseRepository
import com.woory.data.source.DatabaseDataSource
import com.woory.data.source.FirebaseDataSource
import com.woory.data.source.NetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        databaseDataSource: DatabaseDataSource,
        firebaseDataSource: FirebaseDataSource,
        networkDataSource: NetworkDataSource
    ): PromiseRepository =
        DefaultPromiseRepository(databaseDataSource, firebaseDataSource, networkDataSource)

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