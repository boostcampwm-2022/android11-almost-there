package com.woory.firebase.model.mapper

interface ModelMapper<Domain, Model> {

    fun asModel(domain: Domain): Model

    fun asDomain(model: Model): Domain
}