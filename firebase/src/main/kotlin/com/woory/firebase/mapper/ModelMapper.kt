package com.woory.firebase.mapper

interface ModelMapper<Domain, Model> {

    fun asModel(domain: Domain): Model

    fun asDomain(model: Model): Domain
}