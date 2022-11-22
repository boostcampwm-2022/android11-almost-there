package com.example.firebase

import android.util.Log
import com.woory.data.model.GeoPointModel
import com.woory.data.model.LocationModel
import com.woory.data.model.PromiseDataModel
import com.woory.data.model.UserModel
import com.woory.data.source.FirebaseDataSource
import com.woory.firebase.datasource.DefaultFirebaseDataSource
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    private lateinit var firebaseDataSource: FirebaseDataSource

    @Before
    fun setFirebase() {
        firebaseDataSource = DefaultFirebaseDataSource()
    }

    @Test
    fun createPromiseTest() {
        val promiseModel = PromiseDataModel(
            LocationModel(
                GeoPointModel(0.0, 0.0),
                "백악관"
            ),
            OffsetDateTime.of(2022, 11, 20, 12, 20, 0, 0, ZoneOffset.of("+09:00")),
            OffsetDateTime.of(2022, 11, 20, 12, 20, 0, 0, ZoneOffset.of("+09:00")),
            UserModel(
                "", "바이든"
            ),
            listOf(
                UserModel("", "바이든"),
                UserModel("", "트럼프"),
                UserModel("", "오바마")
            )
        )
        assertEquals(false, firebaseDataSource.createPromise(promiseModel).isSuccess)
    }

    @Test
    fun getPromiseTest(){
        val res = firebaseDataSource.getPromiseByCode("")
        assertEquals(true, res)
    }
}