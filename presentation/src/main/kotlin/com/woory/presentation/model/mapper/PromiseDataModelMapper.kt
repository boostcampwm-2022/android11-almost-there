package com.woory.presentation.model.mapper

import com.woory.presentation.model.PromiseDataModel

object PromiseDataModelMapper :
    UiStateMapper<PromiseDataModel, com.woory.data.model.PromiseDataModel> {

    override fun asUiState(domain: com.woory.data.model.PromiseDataModel): PromiseDataModel =
        PromiseDataModel(
            code = domain.code,
            promiseLocation = domain.promiseLocation.asUiState(),
            promiseDateTime = domain.promiseDateTime,
            gameDateTime = domain.gameDateTime,
            host = domain.host.asUiState(),
            users = domain.users.map { it.asUiState() }
        )

    override fun asDomain(uiState: PromiseDataModel): com.woory.data.model.PromiseDataModel =
        com.woory.data.model.PromiseDataModel(
            code = uiState.code,
            promiseLocation = uiState.promiseLocation.asDomain(),
            promiseDateTime = uiState.promiseDateTime,
            gameDateTime = uiState.gameDateTime,
            host = uiState.host.asDomain(),
            users = uiState.users.map { it.asDomain() }
        )
}

fun PromiseDataModel.asDomain(): com.woory.data.model.PromiseDataModel =
    PromiseDataModelMapper.asDomain(this)

fun com.woory.data.model.PromiseDataModel.asDomain(): PromiseDataModel =
    PromiseDataModelMapper.asUiState(this)