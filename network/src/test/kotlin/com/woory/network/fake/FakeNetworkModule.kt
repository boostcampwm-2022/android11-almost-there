package com.woory.network.fake

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.woory.data.source.NetworkDataSource
import com.woory.network.BuildConfig
import com.woory.network.TMapService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

object FakeNetworkModule {

    private const val BASE_URL = "https://apis.openapi.sk.com"

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    class TMapInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest =
                request().newBuilder().addHeader("appKey", BuildConfig.MAP_API_KEY).build()
            proceed(newRequest)
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().run {
            addInterceptor(TMapInterceptor())
            build()
        }
    }

    fun provideMapService(): TMapService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TMapService::class.java)

    fun provideNetworkDataSource(tMapService: TMapService): NetworkDataSource {
        return FakeNetworkDataSource(tMapService)
    }
}