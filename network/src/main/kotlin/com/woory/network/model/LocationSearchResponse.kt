package com.woory.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationSearchResponse(
    @field:Json(name = "addressInfo") val searchPoiInfo: SearchPoiInfo,
)

@JsonClass(generateAdapter = true)
data class SearchPoiInfo(
    @field:Json(name = "totalCount") val totalCount: String,
    @field:Json(name = "count") val count: String,
    @field:Json(name = "page") val page: String,
    @field:Json(name = "pois") val pois: PoiList
)

@JsonClass(generateAdapter = true)
data class PoiList(
    @field:Json(name = "poi") val poi: List<Poi>
)

@JsonClass(generateAdapter = true)
data class Poi(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "noorLat") val noorLat: String,
    @field:Json(name = "noorLon") val noorLon: String,
    @field:Json(name = "upperAddrName") val upperAddrName: String,
    @field:Json(name = "middleAddrName") val middleAddrName: String,
    @field:Json(name = "lowerAddrName") val lowerAddrName: String,
    @field:Json(name = "detailAddrName") val detailAddrName: String,
    @field:Json(name = "newAddressList") val newAddressList: NewAddressList,
)

@JsonClass(generateAdapter = true)
data class NewAddressList(
    @field:Json(name = "newAddress") val newAddress: List<NewAddress>
)

@JsonClass(generateAdapter = true)
data class NewAddress(
    @field:Json(name = "centerLat") val centerLat: String,
    @field:Json(name = "centerLon") val centerLon: String,
    @field:Json(name = "fullAddressRoad") val fullAddressRoad: String,
)
