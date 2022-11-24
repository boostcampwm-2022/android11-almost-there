package com.woory.presentation.background.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity

class AlarmTouchReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (context == null) {
            return
        }

        val alarmType = intent?.extras?.getInt("ALARM_TYPE") ?: return
        val promiseCode = intent?.extras?.getString("PROMISE_CODE") ?: return

        when (alarmType) {
            AlarmFunctions.PROMISE_READY -> {
                onTouchPromiseReadyNotification(context, promiseCode)
            }
            else -> throw IllegalArgumentException("is not available alarm type")
        }

    }

    private fun onTouchPromiseReadyNotification(context: Context, promiseCode: String) {
        val intent = Intent(context, PromiseInfoActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("PROMISE_CODE_KEY", promiseCode)
        }
        context.startActivity(intent)
    }
}