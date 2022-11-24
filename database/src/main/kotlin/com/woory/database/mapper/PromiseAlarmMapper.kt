package com.woory.database.mapper

import com.woory.data.model.PromiseAlarmModel
import com.woory.data.model.PromiseModel
import com.woory.database.entity.PromiseAlarmEntity

fun PromiseModel.toPromiseAlarmEntity() =
    PromiseAlarmEntity(
        promiseCode = code,
        status = "READY",
        startTime = data.gameDateTime,
        endTime = data.promiseDateTime,
    )

fun PromiseAlarmEntity.toPromiseAlarmModel() =
    PromiseAlarmModel(
        alarmCode = alarmCode,
        promiseCode = promiseCode,
        status = status,
        startTime = startTime,
        endTime = endTime
    )

fun List<PromiseAlarmEntity>.toPromiseAlarmModel() =
    map {
        it.toPromiseAlarmModel()
    }