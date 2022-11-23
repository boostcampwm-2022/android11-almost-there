package com.woory.presentation.background.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import org.threeten.bp.OffsetDateTime

class AlarmFunctions(private val context: Context) {

    fun registerAlarm(dateTime: OffsetDateTime, alarmType: Int, alarmCode: Int, promiseCode: String) {
        val timeInMillis = dateTime.toInstant().toEpochMilli()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val receiverIntent = Intent(context, AlarmReceiver::class.java)
        receiverIntent.apply {
            putExtra("ALARM_CODE", alarmCode)
            putExtra("ALARM_TYPE", alarmType)
            putExtra("PROMISE_CODE", promiseCode)
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(
                context,
                alarmCode,
                receiverIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                alarmCode,
                receiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

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
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        alarmManager.cancel(pendingIntent)
    }

    companion object {
        const val PROMISE_READY = 0
        const val PROMISE_START = 1
    }
}