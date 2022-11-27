package com.woory.presentation.background.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.woory.almostthere.background.notification.NotificationChannelProvider
import com.woory.presentation.R
import com.woory.presentation.background.notification.NotificationProvider
import com.woory.presentation.ui.promises.PromisesActivity

class PromiseGameService : Service() {

    override fun onCreate() {
        super.onCreate()
        val intent = Intent(this, PromisesActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            NotificationProvider.PROMISE_START_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationProvider.createNotificationBuilder(
            this,
            NotificationChannelProvider.PROMISE_START_CHANNEL_ID,
            getString(R.string.notification_start_title),
            getString(R.string.notification_start_content),
            NotificationCompat.PRIORITY_HIGH,
            pendingIntent,
        ).build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationChannelProvider.providePromiseStartChannel(this)
        }
        startForeground(NotificationProvider.PROMISE_START_NOTIFICATION_ID, notification)
    }

    override fun onBind(p0: Intent?): IBinder? = null

}