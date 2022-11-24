package com.woory.firebase.mapper

import com.woory.data.model.UserImage
import com.woory.data.model.UserModel
import com.woory.firebase.model.PromiseParticipantField
import com.woory.firebase.model.UserImageInfoField

internal fun UserModel.toPromiseParticipant() = PromiseParticipantField(
    userImage = this.image.toUserImageInfo(),
    userName = this.name,
    userId = this.id
)

internal fun UserImage.toUserImageInfo() = UserImageInfoField(
    color = this.color,
    imageIdx = this.imageIdx
)

internal fun PromiseParticipantField.toUserModel() = UserModel(
    userId = this.userId,
    userName = this.userName,
    userImage = this.userImage.toUserImage()
)

internal fun UserImageInfoField.toUserImage() = UserImage(
    color = this.color,
    imageIdx = this.imageIdx
)