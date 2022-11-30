package com.woory.firebase.mapper

import com.google.firebase.firestore.GeoPoint
import com.woory.data.model.GeoPointModel
import com.woory.data.model.MagneticInfoModel
import com.woory.firebase.model.MagneticInfoDocument
import com.woory.firebase.util.TimeConverter.asOffsetDate

object MagneticDataMapper : ModelMapper<MagneticInfoModel, MagneticInfoDocument> {
    override fun asModel(domain: MagneticInfoModel): MagneticInfoDocument =
        MagneticInfoDocument(
            GeoPoint(
                domain.centerPoint.latitude,
                domain.centerPoint.longitude
            ),
            domain.radius
        )

    override fun asDomain(model: MagneticInfoDocument): MagneticInfoModel =
        MagneticInfoModel(
            GeoPointModel(
                model.centerPoint.latitude,
                model.centerPoint.longitude
            ),
            model.radius,
            model.timeStamp.asOffsetDate()
        )
}

internal fun MagneticInfoDocument.asDomain(): MagneticInfoModel =
    MagneticDataMapper.asDomain(this)

internal fun MagneticInfoModel.asModel(): MagneticInfoDocument =
    MagneticDataMapper.asModel(this)