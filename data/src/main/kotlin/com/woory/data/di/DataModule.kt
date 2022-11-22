package com.woory.data.di

import com.woory.data.repository.DefaultPromiseRepository
import com.woory.data.repository.PromiseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsRepository(impl: DefaultPromiseRepository): PromiseRepository
}