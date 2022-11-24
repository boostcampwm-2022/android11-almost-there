package com.woory.firebase.mapper

// TODO : Mapper 함수 수정하기
interface ModelMapper<Domain, Model> {

    fun asModel(domain: Domain): Model

    fun asDomain(model: Model): Domain
}