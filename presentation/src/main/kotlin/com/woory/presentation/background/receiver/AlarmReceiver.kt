package com.woory.presentation.background.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.woory.presentation.background.notification.NotificationChannelProvider
import com.woory.presentation.background.notification.NotificationProvider
import com.woory.presentation.R
import com.woory.presentation.background.service.PromiseAlarmRegisterService
import com.woory.presentation.background.service.PromiseGameService
import com.woory.presentation.background.util.asPromiseAlarm
import com.woory.presentation.background.util.putPromiseAlarm
import com.woory.presentation.background.util.startServiceBp
import com.woory.presentation.model.AlarmState
import com.woory.presentation.model.PromiseAlarm

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        val promiseAlarm = intent.asPromiseAlarm()

        when (promiseAlarm.state) {
            AlarmState.READY -> {
                onReceivePromiseReady(context, promiseAlarm)
            }
            AlarmState.START -> {
                onReceivePromiseStart(context, promiseAlarm)
            }
            AlarmState.END -> {

            }
        }
    }

    private fun onReceivePromiseStart(context: Context, promiseAlarm: PromiseAlarm) {
        Intent(context, PromiseAlarmRegisterService::class.java).run {
            putPromiseAlarm(promiseAlarm.copy(state = AlarmState.END))
            context.startServiceBp(this)
        }

        Intent(context, PromiseGameService::class.java).run {
            putPromiseAlarm(promiseAlarm)
            context.startServiceBp(this)
        }
    }

    private fun onReceivePromiseReady(
        context: Context,
        promiseAlarm: PromiseAlarm
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationChannelProvider.providePromiseReadyChannel(context)
        }

        NotificationProvider.notifyBroadcastNotification(
            context,
            context.getString(R.string.notification_ready_title),
            context.getString(R.string.notification_ready_content),
            promiseAlarm
        )
    }
}