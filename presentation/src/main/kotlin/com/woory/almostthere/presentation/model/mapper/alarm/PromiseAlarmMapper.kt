package com.woory.almostthere.presentation.model.mapper.alarm

import com.woory.almostthere.data.model.PromiseAlarmModel
import com.woory.almostthere.presentation.model.PromiseAlarm
import com.woory.almostthere.presentation.model.asAlarmState
import com.woory.almostthere.presentation.model.mapper.UiModelMapper

object PromiseAlarmMapper : UiModelMapper<PromiseAlarm, PromiseAlarmModel> {

    override fun asDomain(uiModel: PromiseAlarm): PromiseAlarmModel =
        PromiseAlarmModel(
            alarmCode = uiModel.alarmCode,
            promiseCode = uiModel.promiseCode,
            status = uiModel.state.current,
            startTime = uiModel.startTime,
            endTime = uiModel.endTime
        )

    override fun asUiModel(domain: PromiseAlarmModel): PromiseAlarm =
        PromiseAlarm(
            alarmCode = domain.alarmCode,
            promiseCode = domain.promiseCode,
            state = domain.status.asAlarmState(),
            startTime = domain.startTime,
            endTime = domain.endTime
        )
}

fun PromiseAlarm.asDomain(): PromiseAlarmModel = PromiseAlarmMapper.asDomain(this)

fun PromiseAlarmModel.asUiModel(): PromiseAlarm = PromiseAlarmMapper.asUiModel(this)