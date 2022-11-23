package com.woory.presentation.model.mapper.alarm

import com.woory.data.model.PromiseAlarmModel
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.model.mapper.UiModelMapper

object PromiseAlarmMapper : UiModelMapper<PromiseAlarm, PromiseAlarmModel> {

    override fun asDomain(uiModel: PromiseAlarm): PromiseAlarmModel =
        PromiseAlarmModel(
            alarmCode = uiModel.alarmCode,
            promiseCode = uiModel.promiseCode,
            status = uiModel.status,
            startTime = uiModel.startTime,
            endTime = uiModel.endTime
        )

    override fun asUiModel(domain: PromiseAlarmModel): PromiseAlarm =
        PromiseAlarm(
            alarmCode = domain.alarmCode,
            promiseCode = domain.promiseCode,
            status = domain.status,
            startTime = domain.startTime,
            endTime = domain.endTime
        )
}

fun PromiseAlarm.asDomain(): PromiseAlarmModel = PromiseAlarmMapper.asDomain(this)

fun PromiseAlarmModel.asUiModel(): PromiseAlarm = PromiseAlarmMapper.asUiModel(this)