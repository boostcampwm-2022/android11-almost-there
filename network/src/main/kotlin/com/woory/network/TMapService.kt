package com.woory.network

import com.woory.network.model.AddressInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

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
}