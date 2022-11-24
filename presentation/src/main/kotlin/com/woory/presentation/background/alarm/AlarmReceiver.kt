package com.woory.presentation.background.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.woory.almostthere.background.notification.NotificationChannelProvider
import com.woory.presentation.background.notification.NotificationProvider
import com.woory.presentation.R
import com.woory.presentation.background.util.asPromiseAlarm
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.util.ALARM_STATUS_END
import com.woory.presentation.util.ALARM_STATUS_READY
import com.woory.presentation.util.ALARM_STATUS_START

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: throw IllegalArgumentException()
        intent ?: throw IllegalArgumentException()

        val promiseAlarm = intent.asPromiseAlarm()

        when (promiseAlarm.status) {
            ALARM_STATUS_READY -> {
                onReceivePromiseReady(context, promiseAlarm)
            }
            ALARM_STATUS_START -> {

            }
            ALARM_STATUS_END -> {

            }
            else -> throw IllegalArgumentException(context.getString(R.string.notification_invalid_type))
        }
    }

    private fun onReceivePromiseReady(
        context: Context,
        promiseAlarm: PromiseAlarm
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationChannelProvider.providePromiseReadyChannel(context)
        }

        NotificationProvider.notifyPromiseReadyNotification(
            context,
            context.getString(R.string.notification_ready_title),
            context.getString(R.string.notification_ready_content),
            promiseAlarm
        )
    }
}