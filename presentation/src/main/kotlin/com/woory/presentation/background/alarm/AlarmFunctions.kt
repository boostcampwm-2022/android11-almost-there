package com.woory.presentation.background.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.woory.presentation.background.util.putPromiseAlarm
import com.woory.presentation.model.AlarmState
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.util.TimeConverter.asMillis

class AlarmFunctions(private val context: Context) {

    fun registerAlarm(
        promiseAlarm: PromiseAlarm
    ) {
        val timeInMillis = when (promiseAlarm.state) {
//            ALARM_STATUS_READY -> promiseAlarm.startTime.minusMinutes(5)
            // Todo :: 테스트용 코드
            AlarmState.READY -> promiseAlarm.startTime.minusSeconds(5)
            AlarmState.START -> promiseAlarm.startTime
            AlarmState.END -> promiseAlarm.endTime
        }.asMillis()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val receiverIntent = Intent(context, AlarmReceiver::class.java)
        receiverIntent.apply {
            putPromiseAlarm(promiseAlarm)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            promiseAlarm.alarmCode,
            receiverIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent
        )
    }

    fun cancelAlarm(alarmCode: Int) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(context, alarmCode, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(
                context,
                alarmCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

        alarmManager.cancel(pendingIntent)
    }
}