package com.woory.presentation.background.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.woory.almostthere.background.notification.NotificationChannelProvider
import com.woory.presentation.R
import com.woory.presentation.background.notification.NotificationProvider
import com.woory.presentation.background.service.PromiseGameService
import com.woory.presentation.background.util.asPromiseAlarm
import com.woory.presentation.background.util.putPromiseAlarm
import com.woory.presentation.model.AlarmState
import com.woory.presentation.model.PromiseAlarm

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: throw IllegalArgumentException("is context null")
        intent ?: throw IllegalArgumentException("is intent null")

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationChannelProvider.providePromiseStartChannel(context)
        }
        Intent(context, PromiseGameService::class.java).run {
            putPromiseAlarm(promiseAlarm)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(this)
            } else {
                context.startService(this)
            }
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