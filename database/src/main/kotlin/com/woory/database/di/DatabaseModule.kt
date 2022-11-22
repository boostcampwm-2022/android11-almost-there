package com.woory.database.di

import com.woory.data.source.DatabaseDataSource
import com.woory.database.DefaultDatabaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseDataSource(): DatabaseDataSource = DefaultDatabaseDataSource()
}