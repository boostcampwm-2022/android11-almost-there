package com.woory.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddressInfoResponse(
    @field:Json(name = "addressInfo") val addressInfo: AddressInfo,
)

@JsonClass(generateAdapter = true)
data class AddressInfo(
    @field:Json(name = "fullAddress") val fullAddress: String,
    @field:Json(name = "addressType") val addressType: String,
    @field:Json(name = "city_do") val city_do: String,
    @field:Json(name = "gu_gun") val gu_gun: String,
    @field:Json(name = "eup_myun") val eup_myun: String,
    @field:Json(name = "adminDong") val adminDong: String,
    @field:Json(name = "adminDongCode") val adminDongCode: String,
    @field:Json(name = "legalDong") val legalDong: String,
    @field:Json(name = "legalDongCode") val legalDongCode: String,
    @field:Json(name = "ri") val ri: String,
    @field:Json(name = "bunji") val bunji: String,
    @field:Json(name = "roadName") val roadName: String,
    @field:Json(name = "buildingIndex") val buildingIndex: String,
    @field:Json(name = "buildingName") val buildingName: String,
    @field:Json(name = "mappingDistance") val mappingDistance: String,
    @field:Json(name = "roadCode") val roadCode: String
)
