package com.woory.presentation.di

import androidx.lifecycle.SavedStateHandle
import com.woory.presentation.ui.history.PromiseHistoryType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object PromiseHistoryModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class HistoryType

    @ViewModelScoped
    @HistoryType
    @Provides
    fun providePromiseHistoryType(savedStateHandle: SavedStateHandle): PromiseHistoryType =
        savedStateHandle["promise_history_type"] ?: throw IllegalStateException()
}