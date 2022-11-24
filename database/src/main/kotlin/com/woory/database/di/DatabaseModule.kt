package com.woory.database.di

import android.content.Context
import androidx.room.Room
import com.woory.data.source.DatabaseDataSource
import com.woory.database.AppDatabase
import com.woory.database.PromiseAlarmDao
import com.woory.database.datasource.DefaultDatabaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "promise_time.db"
        ).build()

    @Provides
    fun providePromiseDao(database: AppDatabase): PromiseAlarmDao {
        return database.promiseDao()
    }

    @Singleton
    @Provides
    fun provideDatabaseDataSource(promiseDao: PromiseAlarmDao): DatabaseDataSource =
        DefaultDatabaseDataSource(promiseDao)
}