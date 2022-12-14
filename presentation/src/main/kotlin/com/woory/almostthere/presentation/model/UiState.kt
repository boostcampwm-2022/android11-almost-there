package com.woory.almostthere.presentation.model

import com.woory.almostthere.presentation.model.exception.AlmostThereException

sealed class UiState<out T> {

    object Loading : UiState<Nothing>()

    data class Error(val errorType: AlmostThereException) : UiState<Nothing>()

    data class Success<T>(val data: T) : UiState<T>()
}