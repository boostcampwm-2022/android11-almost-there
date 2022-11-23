package com.woory.presentation.background.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            return
        }
        val alarmType = intent.extras?.getInt("ALARM_TYPE")
        when (alarmType) {
            AlarmFunctions.PROMISE_READY -> {}
            AlarmFunctions.PROMISE_START -> {}
            else -> throw IllegalArgumentException("is not available alarm type")
        }
    }
}