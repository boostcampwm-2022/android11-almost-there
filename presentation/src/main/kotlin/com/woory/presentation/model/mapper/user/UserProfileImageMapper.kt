package com.woory.presentation.model.mapper.user

import com.woory.data.model.UserProfileImageModel
import com.woory.presentation.model.UserProfileImage
import com.woory.presentation.model.mapper.UiModelMapper

object UserProfileImageMapper : UiModelMapper<UserProfileImage, UserProfileImageModel> {

    override fun asUiModel(domain: UserProfileImageModel): UserProfileImage = UserProfileImage(
        backgroundColor = domain.backgroundColor,
        imageIndex = domain.imageIndex
    )

    override fun asDomain(uiModel: UserProfileImage): UserProfileImageModel =
        UserProfileImageModel(
            backgroundColor = uiModel.backgroundColor,
            imageIndex = uiModel.imageIndex
        )
}

fun UserProfileImage.asDomain(): UserProfileImageModel = UserProfileImageMapper.asDomain(this)

fun UserProfileImageModel.asUiModel(): UserProfileImage = UserProfileImageMapper.asUiModel(this)