package com.woory.network

import com.woory.data.model.GeoPointModel
import com.woory.data.source.NetworkDataSource
import com.woory.network.fake.FakeNetworkModule
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class NetworkDataSourceUnitTest {

    private var dataSource: NetworkDataSource? = null

    @Before
    fun setDataSource() {
        val service = FakeNetworkModule.provideMapService()
        dataSource = FakeNetworkModule.provideNetworkDataSource(service)
    }


    suspend fun getAddressByPoint(geoPoint: GeoPointModel): Result<String> {
        return dataSource?.getAddressByPoint(geoPoint) ?: Result.failure(throw Exception("null"))
    }

    @Test
    fun getAddressByPoint_valid() {
        runTest {
            val geoPoint = GeoPointModel(37.56648210, 126.98502043)
            getAddressByPoint(geoPoint).onSuccess {
                println(it)
            }.onFailure {
                println(it.toString())
            }
        }
    }
}