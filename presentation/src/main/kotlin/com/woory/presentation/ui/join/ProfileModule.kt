package com.woory.presentation.ui.join

import androidx.lifecycle.SavedStateHandle
import com.woory.presentation.ui.join.ProfileActivity.Companion.PROMISE_CODE_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object ProfileModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Code

    @ViewModelScoped
    @Code
    @Provides
    fun provideCode(savedStateHandle: SavedStateHandle): String? =
        savedStateHandle[PROMISE_CODE_KEY]
}