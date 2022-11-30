package com.woory.presentation.model.mapper.magnetic

import com.woory.data.model.GeoPointModel
import com.woory.data.model.MagneticInfoModel
import com.woory.presentation.model.GeoPoint
import com.woory.presentation.model.MagneticInfo
import com.woory.presentation.model.mapper.UiModelMapper

object MagneticInfoMapper : UiModelMapper<MagneticInfo, MagneticInfoModel> {
    override fun asUiModel(domain: MagneticInfoModel): MagneticInfo =
        MagneticInfo(
            GeoPoint(
                domain.centerPoint.latitude,
                domain.centerPoint.longitude
            ),
            domain.radius,
            domain.updatedAt
        )

    override fun asDomain(uiModel: MagneticInfo): MagneticInfoModel =
        MagneticInfoModel(
            GeoPointModel(
                uiModel.centerPoint.latitude,
                uiModel.centerPoint.longitude
            ),
            uiModel.radius,
            uiModel.updatedAt
        )
}

internal fun MagneticInfo.asDomain(): MagneticInfoModel =
    MagneticInfoMapper.asDomain(this)

internal fun MagneticInfoModel.asUiModel(): MagneticInfo =
    MagneticInfoMapper.asUiModel(this)