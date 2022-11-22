package com.woory.firebase.mapper

import com.woory.data.model.UserImage
import com.woory.data.model.UserModel
import com.woory.firebase.model.PromiseParticipant
import com.woory.firebase.model.UserImageInfo

internal fun UserModel.toPromiseParticipant() = PromiseParticipant(
    UserImage = this.image.toUserImageInfo(),
    UserName = this.name,
    UserId = this.id
)

internal fun UserImage.toUserImageInfo() = UserImageInfo(
    Color = this.color,
    ImageIdx = this.imageIdx
)

internal fun PromiseParticipant.toUserModel() = UserModel(
    id = this.UserId,
    name = this.UserName,
    image = this.UserImage.toUserImage()
)

internal fun UserImageInfo.toUserImage() = UserImage(
    color = this.Color,
    imageIdx = this.ImageIdx
)