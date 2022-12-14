package com.woory.almostthere.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.woory.almostthere.data.source.DatabaseDataSource
import com.woory.almostthere.database.AlmostThereDatabase
import com.woory.almostthere.database.PromiseAlarmDao
import com.woory.almostthere.database.source.DefaultDatabaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val USER_PREFERENCES = "user_preferences"

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AlmostThereDatabase = Room
        .databaseBuilder(application, AlmostThereDatabase::class.java, "AlmostThere.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun providePromiseAlarmDao(appDatabase: AlmostThereDatabase): PromiseAlarmDao =
        appDatabase.promiseAlarmDao()

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(produceFile = {
            context.preferencesDataStoreFile(USER_PREFERENCES)
        })

    @Provides
    @Singleton
    fun provideDatabaseDataSource(
        promiseAlarmDao: PromiseAlarmDao,
        dataStore: DataStore<Preferences>
    ): DatabaseDataSource =
        DefaultDatabaseDataSource(promiseAlarmDao, dataStore)
}