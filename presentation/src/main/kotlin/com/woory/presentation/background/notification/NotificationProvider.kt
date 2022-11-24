package com.woory.almostthere.background.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.woory.presentation.R
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity

object NotificationProvider {
    const val PROMISE_READY_NOTIFICATION_ID = 80

    private fun createNotificationBuilder(
        context: Context,
        channelId: String,
        title: String,
        content: String,
        priority: Int,
        intent: Intent,
        requestCode: Int = 0,
    ): NotificationCompat.Builder {

        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )


        return NotificationCompat.Builder(context, channelId).apply {
            // Todo :: 앱 아이콘 변경 필요
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(title)
            setContentText(content)
            setAutoCancel(true)
            this.priority = priority

            if (priority == NotificationCompat.PRIORITY_HIGH) {
                setFullScreenIntent(pendingIntent, true)
            } else {
                setContentIntent(pendingIntent)
            }
        }
    }

    fun notifyPromiseReadyNotification(
        context: Context,
        title: String,
        content: String,
        promiseCode: String
    ) {
        val notificationManager = NotificationManagerCompat.from(context)
        val intent = Intent(context, PromiseInfoActivity::class.java)
        intent.putExtra("PROMISE_CODE", promiseCode)

        val notification = createNotificationBuilder(
            context,
            NotificationChannelProvider.PROMISE_CHANNEL_ID,
            title,
            content,
            NotificationCompat.PRIORITY_HIGH,
            intent,
        )

        notificationManager.notify(PROMISE_READY_NOTIFICATION_ID, notification.build())
    }

}