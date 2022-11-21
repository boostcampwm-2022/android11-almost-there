package com.woory.almostthere.ui.promises

import androidx.lifecycle.ViewModel
import com.woory.almostthere.model.GeoPointModel
import com.woory.almostthere.model.LocationModel
import com.woory.almostthere.model.PromiseDataModel
import com.woory.almostthere.model.UserModel
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset

class PromisesViewModel : ViewModel() {

    val dummyPromises = listOf(
        PromiseDataModel(
            promiseLocation = LocationModel(GeoPointModel(0.0, 0.0), "경기 성남시 분당구 판교역로 166"),
            promiseDateTime = OffsetDateTime.of(
                2022,
                11,
                19,
                12,
                30,
                0,
                0,
                ZoneOffset.of("+09:00")
            ),
            gameDateTime = OffsetDateTime.of(2022, 11, 19, 12, 0, 0, 0, ZoneOffset.of("+09:00")),
            host = UserModel("김또깡", "123"),
            users = listOf(UserModel("김또깡", "123")),
        ),
        PromiseDataModel(
            promiseLocation = LocationModel(GeoPointModel(0.0, 0.0), "경기 성남시 분당구 판교역로 166"),
            promiseDateTime = OffsetDateTime.of(
                2022,
                11,
                20,
                12,
                30,
                0,
                0,
                ZoneOffset.of("+09:00")
            ),
            gameDateTime = OffsetDateTime.of(2022, 11, 20, 12, 0, 0, 0, ZoneOffset.of("+09:00")),
            host = UserModel("김또깡", "123"),
            users = listOf(UserModel("김또깡", "123")),
        ),
        PromiseDataModel(
            promiseLocation = LocationModel(GeoPointModel(0.0, 0.0), "경기 성남시 분당구 판교역로 166"),
            promiseDateTime = OffsetDateTime.of(
                2022,
                11,
                21,
                12,
                30,
                0,
                0,
                ZoneOffset.of("+09:00")
            ),
            gameDateTime = OffsetDateTime.of(2022, 11, 21, 12, 0, 0, 0, ZoneOffset.of("+09:00")),
            host = UserModel("김또깡", "123"),
            users = listOf(UserModel("김또깡", "123")),
        ),
    )
}