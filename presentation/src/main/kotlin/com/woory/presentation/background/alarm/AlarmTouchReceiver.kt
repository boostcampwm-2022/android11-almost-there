package com.woory.presentation.background.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.woory.presentation.background.service.PromiseReadyService
import com.woory.presentation.background.util.asPromiseAlarm
import com.woory.presentation.background.util.putPromiseAlarm
import com.woory.presentation.background.util.startServiceBp
import com.woory.presentation.model.AlarmState
import com.woory.presentation.model.PromiseAlarm

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
        Intent(context, PromiseReadyService::class.java).run {
            putPromiseAlarm(promiseAlarm)
            context.startServiceBp(this)
        }
    }
}