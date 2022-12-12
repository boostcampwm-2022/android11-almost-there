package com.woory.presentation.background.notification

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.woory.presentation.R
import com.woory.presentation.background.receiver.AlarmReceiver
import com.woory.presentation.background.receiver.AlarmTouchReceiver
import com.woory.presentation.background.util.putPromiseAlarm
import com.woory.presentation.model.PromiseAlarm

object NotificationProvider {
    const val PROMISE_READY_NOTIFICATION_ID = 80
    const val PROMISE_READY_COMPLETE_NOTIFICATION_ID = 81
    const val PROMISE_START_NOTIFICATION_ID = 82

    fun createNotificationBuilder(
        context: Context,
        channelId: String,
        title: String,
        content: String?,
        priority: Int,
        pendingIntent: PendingIntent?,
    ): NotificationCompat.Builder {

        return NotificationCompat.Builder(context, channelId).apply {
            // Todo :: 앱 아이콘 변경 필요
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(title)
            setContentText(content)
            setAutoCancel(true)
            this.priority = priority

            setContentIntent(pendingIntent)
        }
    }

    fun notifyBroadcastNotification(
        context: Context,
        title: String,
        content: String,
        promiseAlarm: PromiseAlarm,
    ) {
        val notificationManager = NotificationManagerCompat.from(context)

        val intent = Intent(context, AlarmTouchReceiver::class.java)
        intent.putPromiseAlarm(promiseAlarm)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            promiseAlarm.alarmCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = createNotificationBuilder(
            context,
            NotificationChannelProvider.PROMISE_READY_CHANNEL_ID,
            title,
            content,
            NotificationCompat.PRIORITY_HIGH,
            pendingIntent,
        )
        notificationManager.notify(PROMISE_READY_NOTIFICATION_ID, notification.build())
    }

    fun notifyActivityNotification(
        context: Context,
        title: String,
        content: String,
        promiseAlarm: PromiseAlarm,
        intentClass: Class<*>,
    ) {
        val notificationManager = NotificationManagerCompat.from(context)

        val intent = Intent(context, intentClass)
        intent.putPromiseAlarm(promiseAlarm)

        initActivityPendingIntent(context, promiseAlarm.alarmCode, intentClass)

        val pendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(promiseAlarm.alarmCode, PendingIntent.FLAG_IMMUTABLE)
        } ?: return

        val notification = createNotificationBuilder(
            context,
            NotificationChannelProvider.PROMISE_READY_CHANNEL_ID,
            title,
            content,
            NotificationCompat.PRIORITY_HIGH,
            pendingIntent,
        )
        notificationManager.notify(PROMISE_READY_COMPLETE_NOTIFICATION_ID, notification.build())
    }

    private fun initActivityPendingIntent(context: Context, alarmCode: Int, intentClass: Class<*>) = PendingIntent.getActivity(
        context,
        alarmCode,
        Intent(context, intentClass),
        PendingIntent.FLAG_IMMUTABLE
    ).cancel()
}