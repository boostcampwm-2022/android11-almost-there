package com.woory.network

import com.woory.network.model.AddressInfoResponse
import com.woory.network.model.LocationSearchResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface TMapService {

    @GET("tmap/geo/reversegeocoding")
    suspend fun getReverseGeoCoding(
        @Query("version") version: Int = 1,
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("coordType") coordType: String = "WGS84GEO",
        @Query("addressType") addressType: String = "A03",
        @Query("newAddressExtend") newAddressExtend: String = "Y"
    ): AddressInfoResponse

    @GET("tmap/pois")
    suspend fun getSearchedLocation(
        @Query("version") version: Int = 1,
        @Query("searchKeyword") searchKeyword: String,
        @Query("count") count: Int = 20
    ): LocationSearchResponse

    @POST("tmap/routes")
    suspend fun getCarRoute(
        @Query("version") version: Int = 1,
        @Query("startX") startX: Double,
        @Query("startY") startY: Double,
        @Query("endX") endX: Double,
        @Query("endY") endY: Double,
    ): ResponseBody

    @POST("tmap/routes/pedestrian")
    suspend fun getWalkRoute(
        @Query("version") version: Int = 1,
        @Query("startX") startX: Double,
        @Query("startY") startY: Double,
        @Query("endX") endX: Double,
        @Query("endY") endY: Double,
        @Query("startName") startName: String = "START",
        @Query("endName") endName: String = "END",
        ): ResponseBody
}