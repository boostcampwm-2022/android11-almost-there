package com.woory.presentation.model.mapper

import com.woory.presentation.model.PromiseDataModel

object PromiseDataModelMapper :
    UiStateMapper<PromiseDataModel, com.woory.data.model.PromiseModel> {

    override fun asUiState(domain: com.woory.data.model.PromiseModel): PromiseDataModel =
        PromiseDataModel(
            code = domain.code,
            promiseLocation = domain.data.promiseLocation.asUiState(),
            promiseDateTime = domain.data.promiseDateTime,
            gameDateTime = domain.data.gameDateTime,
            host = domain.data.host.asUiState(),
            users = domain.data.users.map { it.asUiState() }
        )

    override fun asDomain(uiState: PromiseDataModel): com.woory.data.model.PromiseModel =
        com.woory.data.model.PromiseModel(
            code = uiState.code,
            data = com.woory.data.model.PromiseDataModel(
                promiseLocation = uiState.promiseLocation.asDomain(),
                promiseDateTime = uiState.promiseDateTime,
                gameDateTime = uiState.gameDateTime,
                host = uiState.host.asDomain(),
                users = uiState.users.map { it.asDomain() }
            )
        )
}

fun PromiseDataModel.asDomain(): com.woory.data.model.PromiseModel =
    PromiseDataModelMapper.asDomain(this)

fun com.woory.data.model.PromiseModel.asDomain(): PromiseDataModel =
    PromiseDataModelMapper.asUiState(this)