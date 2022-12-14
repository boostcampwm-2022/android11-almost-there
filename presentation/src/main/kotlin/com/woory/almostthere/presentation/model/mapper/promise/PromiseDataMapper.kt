package com.woory.almostthere.presentation.model.mapper.promise

import com.woory.almostthere.data.model.PromiseDataModel
import com.woory.almostthere.presentation.model.PromiseData
import com.woory.almostthere.presentation.model.mapper.UiModelMapper
import com.woory.almostthere.presentation.model.mapper.location.asDomain
import com.woory.almostthere.presentation.model.mapper.location.asUiModel
import com.woory.almostthere.presentation.model.mapper.user.asDomain
import com.woory.almostthere.presentation.model.mapper.user.asUiModel

object PromiseDataMapper : UiModelMapper<PromiseData, PromiseDataModel> {

    override fun asDomain(uiModel: PromiseData): PromiseDataModel =
        PromiseDataModel(
            promiseLocation = uiModel.promiseLocation.asDomain(),
            promiseDateTime = uiModel.promiseDateTime,
            gameDateTime = uiModel.gameDateTime,
            host = uiModel.host.asDomain(),
            users = uiModel.users.map { it.asDomain() }
        )

    override fun asUiModel(domain: PromiseDataModel): PromiseData =
        PromiseData(
            promiseLocation = domain.promiseLocation.asUiModel(),
            promiseDateTime = domain.promiseDateTime,
            gameDateTime = domain.gameDateTime,
            host = domain.host.asUiModel(),
            users = domain.users.map { it.asUiModel() }
        )
}

fun PromiseData.asDomain(): PromiseDataModel = PromiseDataMapper.asDomain(this)

fun PromiseDataModel.asUiModel(): PromiseData = PromiseDataMapper.asUiModel(this)
