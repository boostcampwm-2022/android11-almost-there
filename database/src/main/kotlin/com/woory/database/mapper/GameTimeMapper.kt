package com.woory.database.mapper

import com.woory.data.model.GameTimeInfoModel
import com.woory.database.entity.GameTimeInfoEntity

fun GameTimeInfoModel.toGameTimeInfoEntity() =
    GameTimeInfoEntity(
        code = code,
        startTime = startTime,
        endTime = endTime
    )

fun GameTimeInfoEntity.toGameTimeInfoModel() =
    GameTimeInfoModel(
        code = code,
        startTime = startTime,
        endTime = endTime
    )

fun List<GameTimeInfoEntity>.toGameTimeInfoModels() =
    map {
        it.toGameTimeInfoModel()
    }