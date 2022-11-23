package com.woory.presentation.model.mapper

import com.woory.data.model.UserImage
import com.woory.presentation.model.UserProfileImage

object UserProfileMapper : UiStateMapper<UserProfileImage, com.woory.data.model.UserImage> {

    override fun asUiState(domain: com.woory.data.model.UserImage): UserProfileImage =
        UserProfileImage(
            backgroundColor = domain.color,
            imageIndex = domain.imageIdx
        )

    override fun asDomain(uiState: UserProfileImage): com.woory.data.model.UserImage =
        com.woory.data.model.UserImage(
            color = uiState.backgroundColor,
            imageIdx = uiState.imageIndex
        )
}

fun UserProfileImage.asDomain(): UserImage = UserProfileMapper.asDomain(this)

fun UserImage.asUiState(): UserProfileImage = UserProfileMapper.asUiState(this)