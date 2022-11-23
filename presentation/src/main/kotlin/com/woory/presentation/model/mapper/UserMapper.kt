package com.woory.presentation.model.mapper

import com.woory.presentation.model.UserModel

object UserMapper : UiStateMapper<UserModel, com.woory.data.model.UserModel> {

    override fun asUiState(domain: com.woory.data.model.UserModel): UserModel =
        UserModel(
            name = domain.name,
            profileImage = domain.image.asUiState()
        )

    override fun asDomain(uiState: UserModel): com.woory.data.model.UserModel =
        com.woory.data.model.UserModel(
            id = "",
            name = uiState.name,
            image = uiState.profileImage.asDomain()
        )
}

fun UserModel.asDomain(): com.woory.data.model.UserModel = UserMapper.asDomain(this)

fun com.woory.data.model.UserModel.asUiState(): UserModel = UserMapper.asUiState(this)