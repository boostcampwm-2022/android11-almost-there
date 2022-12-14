package com.woory.almostthere.network.model.mapper

import com.google.firebase.firestore.GeoPoint
import com.woory.almostthere.data.model.GeoPointModel
import com.woory.almostthere.data.model.MagneticInfoModel
import com.woory.almostthere.network.model.MagneticInfoDocument
import com.woory.almostthere.network.util.TimeConverter.asOffsetDate
import com.woory.almostthere.network.util.TimeConverter.asTimeStamp

object MagneticDataMapper : ModelMapper<MagneticInfoModel, MagneticInfoDocument> {
    override fun asModel(domain: MagneticInfoModel): MagneticInfoDocument =
        MagneticInfoDocument(
            gameCode = domain.gameCode,
            centerPoint = GeoPoint(
                domain.centerPoint.latitude,
                domain.centerPoint.longitude
            ),
            radius = domain.radius,
            initialRadius = domain.initialRadius,
            timeStamp = domain.updatedAt.asTimeStamp()
        )

    override fun asDomain(model: MagneticInfoDocument): MagneticInfoModel =
        MagneticInfoModel(
            model.gameCode,
            GeoPointModel(
                model.centerPoint.latitude,
                model.centerPoint.longitude
            ),
            model.radius,
            model.initialRadius,
            model.timeStamp.asOffsetDate()
        )
}

internal fun MagneticInfoDocument.asDomain(): MagneticInfoModel =
    MagneticDataMapper.asDomain(this)

internal fun MagneticInfoModel.asModel(): MagneticInfoDocument =
    MagneticDataMapper.asModel(this)