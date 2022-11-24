package com.woory.presentation.ui.promiseinfo

sealed class PromiseUiState {
    object Loading : PromiseUiState()
    object Success : PromiseUiState()
    class Fail(val message: String) : PromiseUiState()
    object Waiting : PromiseUiState()
}