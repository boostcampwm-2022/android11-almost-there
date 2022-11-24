package com.woory.network.model.mapper

interface ModelMapper<Domain, Model> {

    // 아직 사용하는 코드가 없어서 구현하지 않았습니다.
//    fun asModel(domain: Domain): Model

    fun asDomain(model: Model): Domain
}