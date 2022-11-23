package com.woory.almostthere.model.mapper

import com.woory.almostthere.model.PromiseUiState
import com.woory.data.model.Promise

object PromiseUiStateMapper : UiStateMapper<PromiseUiState, Promise> {

    override fun asUiState(domain: Promise): PromiseUiState =
        PromiseUiState(
            code = domain.code
        )

    override fun asDomain(uiState: PromiseUiState): Promise =
        Promise(
            code = uiState.code
        )
}

fun Promise.asUiState(): PromiseUiState = PromiseUiStateMapper.asUiState(this)

fun PromiseUiState.asDomain(): Promise = PromiseUiStateMapper.asDomain(this)