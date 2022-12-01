package com.woory.network

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ODsayService {

    @GET("searchPubTransPathT")
    suspend fun getPublicTransitRoute(
        @Query("apiKey") apiKey: String,
        @Query("SX") sx: Double,
        @Query("SY") sy: Double,
        @Query("EX") ex: Double,
        @Query("EY") ey: Double,
    ): ResponseBody

}