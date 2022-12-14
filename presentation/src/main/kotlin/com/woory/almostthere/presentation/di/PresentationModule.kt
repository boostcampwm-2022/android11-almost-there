package com.woory.almostthere.presentation.di

import androidx.lifecycle.SavedStateHandle
import com.woory.almostthere.presentation.ui.history.PromiseHistoryType
import com.woory.almostthere.presentation.ui.join.ProfileActivity.Companion.PROMISE_CODE_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object PresentationModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Code

    @Provides
    @ViewModelScoped
    @Code
    fun provideCode(savedStateHandle: SavedStateHandle): String? =
        savedStateHandle[PROMISE_CODE_KEY]

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class HistoryType

    @Provides
    @ViewModelScoped
    @HistoryType
    fun providePromiseHistoryType(savedStateHandle: SavedStateHandle): PromiseHistoryType =
        savedStateHandle["promise_history_type"] ?: throw IllegalStateException()
}