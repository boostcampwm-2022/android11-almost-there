package com.woory.almostthere.presentation.model.mapper

interface UiModelMapper<UiModel, Domain> {

    fun asUiModel(domain: Domain): UiModel

    fun asDomain(uiModel: UiModel): Domain
}