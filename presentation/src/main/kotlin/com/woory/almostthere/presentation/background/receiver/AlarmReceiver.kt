package com.woory.almostthere.presentation.background.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.background.notification.NotificationChannelProvider
import com.woory.almostthere.presentation.background.notification.NotificationProvider
import com.woory.almostthere.presentation.background.service.PromiseAlarmRegisterService
import com.woory.almostthere.presentation.background.service.PromiseFinishService
import com.woory.almostthere.presentation.background.service.PromiseGameService
import com.woory.almostthere.presentation.background.util.asPromiseAlarm
import com.woory.almostthere.presentation.background.util.putPromiseAlarm
import com.woory.almostthere.presentation.background.util.startServiceBp
import com.woory.almostthere.presentation.model.AlarmState
import com.woory.almostthere.presentation.model.PromiseAlarm
import com.woory.almostthere.presentation.ui.promiseinfo.PromiseInfoActivity

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
                onReceivePromiseEnd(context, promiseAlarm)
            }
        }
    }

    private fun onReceivePromiseEnd(context: Context, promiseAlarm: PromiseAlarm) {
        Intent(context, PromiseFinishService::class.java).run {
            putPromiseAlarm(promiseAlarm)
            context.startServiceBp(this)
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

        NotificationProvider.notifyActivityNotification(
            context,
            context.getString(R.string.notification_ready_title),
            context.getString(R.string.notification_ready_content),
            promiseAlarm,
            PromiseInfoActivity::class.java
        )
    }
}