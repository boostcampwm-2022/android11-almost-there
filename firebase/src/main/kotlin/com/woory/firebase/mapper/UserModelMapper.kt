package com.woory.firebase.mapper

import com.woory.data.model.UserImage
import com.woory.data.model.UserModel
import com.woory.firebase.model.PromiseParticipant
import com.woory.firebase.model.UserImageInfo

internal fun UserModel.toPromiseParticipant() = PromiseParticipant(
    userImage = this.userImage.toUserImageInfo(),
    userName = this.userName,
    userId = this.userId
)

internal fun UserImage.toUserImageInfo() = UserImageInfo(
    color = this.color,
    imageIdx = this.imageIdx
)

internal fun PromiseParticipant.toUserModel() = UserModel(
    userId = this.userId,
    userName = this.userName,
    userImage = this.userImage.toUserImage()
)

internal fun UserImageInfo.toUserImage() = UserImage(
    color = this.color,
    imageIdx = this.imageIdx
)