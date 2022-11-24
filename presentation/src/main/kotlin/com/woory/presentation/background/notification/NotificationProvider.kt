package com.woory.almostthere.background.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.woory.presentation.R
import com.woory.presentation.background.alarm.AlarmTouchReceiver
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity

object NotificationProvider {
    const val PROMISE_READY_NOTIFICATION_ID = 80

    @SuppressLint("SuspiciousIndentation")
    private fun createNotificationBuilder(
        context: Context,
        channelId: String,
        title: String,
        content: String,
        priority: Int,
        pendingIntent: PendingIntent,
    ): NotificationCompat.Builder {

        return NotificationCompat.Builder(context, channelId).apply {
            // Todo :: 앱 아이콘 변경 필요
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(title)
            setContentText(content)
            setAutoCancel(true)
            this.priority = priority

//            if (priority == NotificationCompat.PRIORITY_HIGH) {
//                setFullScreenIntent(pendingIntent, true)
//            } else {
                setContentIntent(pendingIntent)
//            }
        }
    }

    fun notifyPromiseReadyNotification(
        context: Context,
        title: String,
        content: String,
        promiseCode: String
    ) {
        val notificationManager = NotificationManagerCompat.from(context)

        val intent = Intent(context, AlarmTouchReceiver::class.java)
        intent.putExtra("PROMISE_CODE", promiseCode)
        intent.putExtra("ALARM_TYPE", "READY")

        val pendingIntent = PendingIntent.getBroadcast(context, PROMISE_READY_NOTIFICATION_ID, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = createNotificationBuilder(
            context,
            NotificationChannelProvider.PROMISE_CHANNEL_ID,
            title,
            content,
            NotificationCompat.PRIORITY_HIGH,
            pendingIntent,
        )

        notificationManager.notify(PROMISE_READY_NOTIFICATION_ID, notification.build())
    }

}