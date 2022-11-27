package com.woory.presentation.ui.promiseinfo

sealed class PromiseUiState {
    object Loading : PromiseUiState()
    object Success : PromiseUiState()
    object Fail : PromiseUiState()
}