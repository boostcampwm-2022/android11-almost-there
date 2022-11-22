package com.woory.almostthere.model.mapper

interface UiStateMapper<UiState, Domain> {

    fun asUiState(domain: Domain): UiState

    fun asDomain(uiState: UiState): Domain
}