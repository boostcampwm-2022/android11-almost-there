package com.woory.database.di

import android.content.Context
import androidx.room.Room
import com.woory.data.source.DatabaseDataSource
import com.woory.database.AppDatabase
import com.woory.database.datasource.DefaultDatabaseDataSource
import com.woory.database.PromiseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "promise_time.db"
        ).build()

    @Provides
    fun providePromiseDao(database: AppDatabase): PromiseDao {
        return database.promiseDao()
    }

    @Provides
    @Singleton
    fun provideDatabaseDataSource(promiseDao: PromiseDao): DatabaseDataSource = DefaultDatabaseDataSource(promiseDao)
}