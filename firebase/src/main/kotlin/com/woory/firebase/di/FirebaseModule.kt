package com.woory.firebase.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.woory.data.source.FirebaseDataSource
import com.woory.firebase.datasource.DefaultFirebaseDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Singleton
    @Provides
    fun provideFireStoreDatabase(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    fun provideFirebaseDataSource(fireStore: FirebaseFirestore): FirebaseDataSource = DefaultFirebaseDataSource(fireStore)
}