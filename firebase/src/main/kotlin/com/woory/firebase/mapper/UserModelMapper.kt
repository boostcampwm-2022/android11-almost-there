package com.woory.firebase.mapper

import com.woory.data.model.UserDataModel
import com.woory.data.model.UserModel
import com.woory.data.model.UserProfileImageModel
import com.woory.firebase.model.PromiseParticipantField
import com.woory.firebase.model.UserImageInfoField

internal fun UserModel.asPromiseParticipant() = PromiseParticipantField(
    userImage = this.data.profileImage.asUserImageField(),
    userName = this.data.name,
    userId = this.userId
)

internal fun UserProfileImageModel.asUserImageField() = UserImageInfoField(
    color = this.color,
    imageIdx = this.imageIndex
)

internal fun PromiseParticipantField.asUserModel() = UserModel(
    userId = userId,
    data = UserDataModel(userName, userImage.asUserImage())
)

internal fun UserImageInfoField.asUserImage() = UserProfileImageModel(
    color = this.color,
    imageIndex = this.imageIdx
)