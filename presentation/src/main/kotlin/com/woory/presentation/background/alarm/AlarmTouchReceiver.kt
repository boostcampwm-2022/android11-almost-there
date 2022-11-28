package com.woory.presentation.background.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.woory.presentation.background.util.asPromiseAlarm
import com.woory.presentation.model.AlarmState
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity
import com.woory.presentation.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmTouchReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: throw IllegalArgumentException("is context null")
        intent ?: throw IllegalArgumentException("is intent null")

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
        val alarmFunctions = AlarmFunctions(context)
        promiseAlarm.copy(state = AlarmState.START).run {
            alarmFunctions.registerAlarm(this)
        }
        val intent = Intent(context, PromiseInfoActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(Constants.PROMISE_CODE_KEY, promiseAlarm.promiseCode)
        }

        context.startActivity(intent)
    }
}