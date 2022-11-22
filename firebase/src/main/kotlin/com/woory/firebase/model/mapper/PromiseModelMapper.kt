package com.woory.firebase.model.mapper

import com.woory.data.model.Promise
import com.woory.firebase.model.PromiseModel

object PromiseModelMapper : ModelMapper<Promise, PromiseModel> {

    override fun asModel(domain: Promise): PromiseModel =
        PromiseModel(
            code = domain.code
        )

    override fun asDomain(model: PromiseModel): Promise =
        Promise(
            code = model.code
        )
}

fun Promise.asModel(): PromiseModel = PromiseModelMapper.asModel(this)

fun PromiseModel.asDomain(): Promise = PromiseModelMapper.asDomain(this)