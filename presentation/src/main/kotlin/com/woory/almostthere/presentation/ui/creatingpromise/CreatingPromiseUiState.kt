package com.woory.almostthere.presentation.ui.creatingpromise

sealed class CreatingPromiseUiState {
    object Success: CreatingPromiseUiState()
    object Loading: CreatingPromiseUiState()
}