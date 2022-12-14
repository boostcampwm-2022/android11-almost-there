package com.woory.almostthere.presentation.model.mapper.user

import com.woory.almostthere.data.model.UserDataModel
import com.woory.almostthere.presentation.model.UserData
import com.woory.almostthere.presentation.model.mapper.UiModelMapper

object UserDataMapper : UiModelMapper<UserData, UserDataModel> {

    override fun asUiModel(domain: UserDataModel): UserData =
        UserData(
            name = domain.name,
            profileImage = domain.profileImage.asUiModel()
        )

    override fun asDomain(uiModel: UserData): UserDataModel =
        UserDataModel(
            name = uiModel.name,
            profileImage = uiModel.profileImage.asDomain()
        )
}

fun UserData.asDomain(): UserDataModel = UserDataMapper.asDomain(this)

fun UserDataModel.asUiModel(): UserData = UserDataMapper.asUiModel(this)