package com.woory.presentation.background.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.woory.presentation.R
import com.woory.presentation.background.util.asPromiseAlarm
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity
import com.woory.presentation.util.ALARM_STATUS_END
import com.woory.presentation.util.ALARM_STATUS_READY
import com.woory.presentation.util.ALARM_STATUS_START

class AlarmTouchReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: throw IllegalArgumentException()
        intent ?: throw IllegalArgumentException()

        val promiseAlarm = intent.asPromiseAlarm()

        when (promiseAlarm.status) {
            ALARM_STATUS_READY -> {
                onTouchPromiseReadyNotification(context, promiseAlarm)
            }
            ALARM_STATUS_START -> {

            }
            ALARM_STATUS_END -> {

            }
            else -> throw IllegalArgumentException(context.getString(R.string.notification_invalid_type))
        }
    }

    private fun onTouchPromiseReadyNotification(
        context: Context,
        promiseAlarm: PromiseAlarm
    ) {
        val alarmFunctions = AlarmFunctions(context)
        alarmFunctions.registerAlarm(promiseAlarm.onStatus(ALARM_STATUS_START))

        val intent = Intent(context, PromiseInfoActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("PROMISE_CODE_KEY", promiseAlarm.promiseCode)
        }
        context.startActivity(intent)
    }
}