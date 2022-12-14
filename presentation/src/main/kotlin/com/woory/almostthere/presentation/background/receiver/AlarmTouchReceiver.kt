package com.woory.almostthere.presentation.background.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.woory.almostthere.presentation.background.service.PromiseAlarmRegisterService
import com.woory.almostthere.presentation.background.service.PromiseReadyService
import com.woory.almostthere.presentation.background.util.asPromiseAlarm
import com.woory.almostthere.presentation.background.util.putPromiseAlarm
import com.woory.almostthere.presentation.background.util.startServiceBp
import com.woory.almostthere.presentation.model.AlarmState
import com.woory.almostthere.presentation.model.PromiseAlarm

class AlarmTouchReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        val promiseAlarm = intent.asPromiseAlarm()

        when (promiseAlarm.state) {
            AlarmState.READY -> {
                onTouchPromiseReadyNotification(context, promiseAlarm)
            }
            AlarmState.START -> {

            }
            AlarmState.END -> {

            }
        }
    }

    private fun onTouchPromiseReadyNotification(
        context: Context,
        promiseAlarm: PromiseAlarm
    ) {
        Intent(context, PromiseAlarmRegisterService::class.java).run {
            putPromiseAlarm(promiseAlarm.copy(state = AlarmState.START))
            context.startServiceBp(this)
        }

        Intent(context, PromiseReadyService::class.java).run {
            putPromiseAlarm(promiseAlarm)
            context.startServiceBp(this)
        }
    }
}