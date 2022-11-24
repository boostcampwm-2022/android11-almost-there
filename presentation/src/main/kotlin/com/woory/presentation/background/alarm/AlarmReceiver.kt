package com.woory.presentation.background.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.woory.almostthere.background.notification.NotificationChannelProvider
import com.woory.almostthere.background.notification.NotificationProvider

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        val alarmType = intent.extras?.getInt("ALARM_TYPE")
        val promiseCode = intent.extras?.getString("PROMISE_CODE")

        when (alarmType) {
            AlarmFunctions.PROMISE_READY -> {
                if (promiseCode != null) {
                    NotificationChannelProvider.providePromiseReadyChannel(context)

                    // Todo :: 알람 메세지 수정 필요
                    NotificationProvider.notifyPromiseReadyNotification(
                        context,
                        "임시 제목",
                        "임시 소제목",
                        promiseCode
                    )
                }
            }
            AlarmFunctions.PROMISE_START -> {}
            else -> throw IllegalArgumentException("is not available alarm type")
        }
    }
}