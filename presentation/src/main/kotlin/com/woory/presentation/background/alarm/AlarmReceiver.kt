package com.woory.presentation.background.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.woory.almostthere.background.notification.NotificationChannelProvider
import com.woory.almostthere.background.notification.NotificationProvider
import com.woory.presentation.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        val alarmType = intent.extras?.getInt("ALARM_TYPE")
        val promiseCode = intent.extras?.getString("PROMISE_CODE")

        when (alarmType) {
            AlarmFunctions.PROMISE_READY -> {
                if (promiseCode != null) {
                    onReceivePromiseReady(context, promiseCode)
                }
            }
            AlarmFunctions.PROMISE_START -> {}
            else -> throw IllegalArgumentException(context.getString(R.string.notification_invalid_type))
        }
    }

    private fun onReceivePromiseReady(context: Context, promiseCode: String) {
        NotificationChannelProvider.providePromiseReadyChannel(context)

        NotificationProvider.notifyPromiseReadyNotification(
            context,
            context.getString(R.string.notification_ready_title),
            context.getString(R.string.notification_ready_content),
            promiseCode
        )
    }
}