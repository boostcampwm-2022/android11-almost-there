package com.woory.almostthere.di

import com.woory.almostthere.data.repository.DefaultPromiseRepository
import com.woory.almostthere.data.repository.DefaultRouteRepository
import com.woory.almostthere.data.repository.DefaultUserRepository
import com.woory.almostthere.data.repository.PromiseRepository
import com.woory.almostthere.data.repository.RouteRepository
import com.woory.almostthere.data.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsPromiseRepository(defaultPromiseRepository: DefaultPromiseRepository): PromiseRepository

    @Binds
    fun bindsUserRepository(defaultUserRepository: DefaultUserRepository): UserRepository

    @Binds
    fun bindsRouteRepository(defaultRouteRepository: DefaultRouteRepository): RouteRepository
}