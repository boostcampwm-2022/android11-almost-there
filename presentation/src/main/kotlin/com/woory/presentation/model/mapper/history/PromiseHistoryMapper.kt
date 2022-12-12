package com.woory.presentation.model.mapper.history

import com.woory.data.model.PromiseHistoryModel
import com.woory.presentation.model.PromiseHistory
import com.woory.presentation.model.mapper.UiModelMapper
import com.woory.presentation.model.mapper.magnetic.asDomain
import com.woory.presentation.model.mapper.magnetic.asUiModel
import com.woory.presentation.model.mapper.promise.asDomain
import com.woory.presentation.model.mapper.promise.asUiModel
import com.woory.presentation.model.mapper.user.asDomain
import com.woory.presentation.model.mapper.user.asUiState

object PromiseHistoryMapper : UiModelMapper<PromiseHistory, PromiseHistoryModel> {

    override fun asDomain(uiModel: PromiseHistory): PromiseHistoryModel =
        PromiseHistoryModel(
            promise = uiModel.promise.asDomain(),
            magnetic = uiModel.magnetic?.asDomain(),
            users = uiModel.users?.map { it.asDomain() }
        )

    override fun asUiModel(domain: PromiseHistoryModel): PromiseHistory =
        PromiseHistory(
            promise = domain.promise.asUiModel(),
            magnetic = domain.magnetic?.asUiModel(),
            users = domain.users?.map { it.asUiState() }
        )

    fun asUiModel(domain: PromiseHistoryModel, userId: String?): PromiseHistory =
        PromiseHistory(
            userId = userId,
            promise = domain.promise.asUiModel(),
            magnetic = domain.magnetic?.asUiModel(),
            users = domain.users?.map { it.asUiState() }
        )
}

fun PromiseHistory.asDomain(): PromiseHistoryModel = PromiseHistoryMapper.asDomain(this)

fun PromiseHistoryModel.asUiModel(userId: String?): PromiseHistory =
    PromiseHistoryMapper.asUiModel(this, userId)