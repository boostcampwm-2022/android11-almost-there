package com.woory.network.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.woory.data.source.NetworkDataSource
import com.woory.network.BuildConfig
import com.woory.network.DefaultNetworkDataSource
import com.woory.network.ODsayService
import com.woory.network.TMapService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://apis.openapi.sk.com"
    private const val ODSAY_URL = "https://api.odsay.com/v1/api/"

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    class ODsayInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest =
                request().newBuilder()
                    .build()
            proceed(newRequest)
        }
    }

    class TMapInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest =
                request().newBuilder().addHeader("appKey", BuildConfig.MAP_API_KEY)
                    .addHeader("accept", "application/json").build()
            proceed(newRequest)
        }
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().run {
            addInterceptor(TMapInterceptor())
            build()
        }
    }

    private fun createODsayOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().run {
            addInterceptor(ODsayInterceptor())
            build()
        }
    }

    @Singleton
    @Provides
    fun provideMapService(): TMapService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TMapService::class.java)

    @Singleton
    @Provides
    fun provideODsayService(): ODsayService =
        Retrofit.Builder()
            .baseUrl(ODSAY_URL)
            .client(createODsayOkHttpClient())
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ODsayService::class.java)

    @Singleton
    @Provides
    fun provideNetworkDataSource(
        tMapService: TMapService,
        oDsayService: ODsayService
    ): NetworkDataSource {
        return DefaultNetworkDataSource(tMapService, oDsayService)
    }
}