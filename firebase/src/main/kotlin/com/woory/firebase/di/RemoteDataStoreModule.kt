package com.woory.firebase.di

import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import com.woory.data.source.remote.RemoteDataStore
import com.woory.firebase.DefaultRemoteDataStore
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataStoreModule {

    @Provides
    @Singleton
    fun provideRemoteDataStore(): RemoteDataStore =
        DefaultRemoteDataStore()
}