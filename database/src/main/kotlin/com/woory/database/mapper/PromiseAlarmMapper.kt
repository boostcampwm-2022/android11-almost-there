package com.woory.database.mapper

import com.woory.data.model.PromiseAlarmModel
import com.woory.data.model.PromiseModel
import com.woory.database.entity.PromiseAlarmEntity

fun PromiseModel.asPromiseAlarmEntity() =
    PromiseAlarmEntity(
        promiseCode = code,
        status = "READY",
        startTime = data.gameDateTime,
        endTime = data.promiseDateTime,
    )

fun PromiseAlarmEntity.asPromiseAlarmModel() =
    PromiseAlarmModel(
        alarmCode = alarmCode,
        promiseCode = promiseCode,
        status = status,
        startTime = startTime,
        endTime = endTime
    )

fun PromiseAlarmModel.asPromiseAlarmEntity() =
    PromiseAlarmEntity(
        alarmCode = alarmCode,
        promiseCode = promiseCode,
        status = status,
        startTime = startTime,
        endTime = endTime
    )

fun List<PromiseAlarmEntity>.asPromiseAlarmModel() =
    map {
        it.asPromiseAlarmModel()
    }