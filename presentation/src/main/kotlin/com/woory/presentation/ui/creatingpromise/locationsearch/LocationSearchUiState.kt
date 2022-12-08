package com.woory.presentation.ui.creatingpromise.locationsearch

sealed class LocationSearchUiState {
    object Success: LocationSearchUiState()
    object Loading: LocationSearchUiState()
}