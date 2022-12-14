package com.woory.almostthere.network.model.mapper

interface ModelMapper<Domain, Model> {

    fun asModel(domain: Domain): Model

    fun asDomain(model: Model): Domain
}