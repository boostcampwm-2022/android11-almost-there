package com.woory.almostthere.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.woory.almostthere.data.source.NetworkDataSource
import com.woory.almostthere.network.BuildConfig
import com.woory.almostthere.network.DefaultNetworkDataSource
import com.woory.almostthere.network.service.ODsayService
import com.woory.almostthere.network.service.TMapService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TMAP_API_BASE_URL = "https://apis.openapi.sk.com"
    private const val ODSAY_API_BASE_URL = "https://api.odsay.com/v1/api/"

    internal class TMapInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("appKey", BuildConfig.MAP_API_KEY)
                .addHeader("accept", "application/json")
                .build()

            proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    @Named("TMap")
    fun provideTMapOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(TMapInterceptor())
            .build()

    @Provides
    @Singleton
    @Named("ODSay")
    fun provideODSayOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    @Named("TMap")
    fun provideTMapRetrofit(@Named("TMap") okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(TMAP_API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    @Named("ODSay")
    fun provideODSayRetrofit(@Named("ODSay") okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(ODSAY_API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideTMapService(@Named("TMap") retrofit: Retrofit): TMapService =
        retrofit.create(TMapService::class.java)

    @Provides
    @Singleton
    fun provideODSayService(@Named("ODSay") retrofit: Retrofit): ODsayService =
        retrofit.create(ODsayService::class.java)

    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideNetworkDataSource(
        tMapService: TMapService,
        ODSayService: ODsayService,
        fireStore: FirebaseFirestore,
        scope: CoroutineScope
    ): NetworkDataSource = DefaultNetworkDataSource(tMapService, ODSayService, fireStore, scope)
}