package com.woory.presentation.background.service

import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.woory.almostthere.background.notification.NotificationChannelProvider
import com.woory.presentation.R
import com.woory.presentation.background.notification.NotificationProvider
import com.woory.presentation.ui.gaming.GamingActivity
import com.woory.presentation.ui.promises.PromisesActivity

class PromiseGameService : Service() {

    override fun onCreate() {
        super.onCreate()
        val intent = Intent(this, GamingActivity::class.java)

        val pendingIntent: PendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(NotificationProvider.PROMISE_START_NOTIFICATION_ID, PendingIntent.FLAG_IMMUTABLE)
        } ?: return

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