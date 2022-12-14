package com.woory.almostthere.presentation.model.mapper.promise

import com.woory.almostthere.data.model.PromiseModel
import com.woory.almostthere.presentation.model.Promise
import com.woory.almostthere.presentation.model.mapper.UiModelMapper

object PromiseMapper :
    UiModelMapper<Promise, PromiseModel> {

    override fun asDomain(uiModel: Promise): PromiseModel =
        PromiseModel(
            code = uiModel.code,
            data = uiModel.data.asDomain()
        )

    override fun asUiModel(domain: PromiseModel): Promise =
        Promise(
            code = domain.code,
            data = domain.data.asUiModel()
        )
}


fun Promise.asDomain(): PromiseModel =
    PromiseMapper.asDomain(this)

fun PromiseModel.asUiModel(): Promise =
    PromiseMapper.asUiModel(this)