package com.woory.firebase.di

import com.woory.data.source.FirebaseDataSource
import com.woory.firebase.datasource.DefaultFirebaseDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindFirebaseDataSource(dataSource: DefaultFirebaseDataSource): FirebaseDataSource
}