package com.woory.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.woory.data.repository.DefaultPromiseRepository
import com.woory.data.repository.DefaultRouteRepository
import com.woory.data.repository.DefaultUserRepository
import com.woory.data.repository.PromiseRepository
import com.woory.data.repository.RouteRepository
import com.woory.data.repository.UserRepository
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
    fun provideUserRepository(dataStore: DataStore<Preferences>): UserRepository =
        DefaultUserRepository(dataStore)

    @Singleton
    @Provides
    fun provideRouteRepository(networkDataSource: NetworkDataSource): RouteRepository =
        DefaultRouteRepository(networkDataSource)
}