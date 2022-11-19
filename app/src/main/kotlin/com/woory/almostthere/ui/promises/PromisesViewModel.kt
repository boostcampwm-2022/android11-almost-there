package com.woory.almostthere.ui.promises

import androidx.lifecycle.ViewModel
import com.woory.almostthere.model.DateModel
import com.woory.almostthere.model.LocationModel
import com.woory.almostthere.model.PromiseInfoModel
import com.woory.almostthere.model.TimeModel
import com.woory.almostthere.model.UserModel

class PromisesViewModel : ViewModel() {

    val dummyPromises = listOf(
        PromiseInfoModel(
            promiseLocation = LocationModel(0.0, 0.0, "경기 성남시 분당구 판교역로 166"),
            promiseDate = DateModel(2022, 1, 2),
            promiseTime = TimeModel(15, 20),
            gameTime = TimeModel(15, 0),
            host = UserModel("김또깡", "123"),
            users = listOf(UserModel("김또깡", "123")),
        ),
        PromiseInfoModel(
            promiseLocation = LocationModel(0.0, 0.0, "경기 성남시 분당구 판교역로 167"),
            promiseDate = DateModel(2022, 1, 2),
            promiseTime = TimeModel(15, 20),
            gameTime = TimeModel(15, 0),
            host = UserModel("Ivy", "123"),
            users = listOf(UserModel("Ivy", "123")),
        )
    )

}