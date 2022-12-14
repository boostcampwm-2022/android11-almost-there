package com.woory.almostthere.presentation.model.mapper.magnetic

import com.woory.almostthere.data.model.GeoPointModel
import com.woory.almostthere.data.model.MagneticInfoModel
import com.woory.almostthere.presentation.model.GeoPoint
import com.woory.almostthere.presentation.model.MagneticInfo
import com.woory.almostthere.presentation.model.mapper.UiModelMapper

object MagneticInfoMapper : UiModelMapper<MagneticInfo, MagneticInfoModel> {
    override fun asUiModel(domain: MagneticInfoModel): MagneticInfo =
        MagneticInfo(
            domain.gameCode,
            GeoPoint(
                domain.centerPoint.latitude,
                domain.centerPoint.longitude
            ),
            domain.radius,
            domain.initialRadius,
            domain.updatedAt
        )

    override fun asDomain(uiModel: MagneticInfo): MagneticInfoModel =
        MagneticInfoModel(
            uiModel.gameCode,
            GeoPointModel(
                uiModel.centerPoint.latitude,
                uiModel.centerPoint.longitude
            ),
            uiModel.radius,
            uiModel.initialRadius,
            uiModel.updatedAt
        )
}

internal fun MagneticInfo.asDomain(): MagneticInfoModel =
    MagneticInfoMapper.asDomain(this)

internal fun MagneticInfoModel.asUiModel(): MagneticInfo =
    MagneticInfoMapper.asUiModel(this)