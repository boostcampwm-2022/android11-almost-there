package com.woory.almostthere.background.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.woory.presentation.R

object NotificationChannelProvider {
    const val PROMISE_READY_CHANNEL_ID = "PromiseReadyNotificationChannel"
    const val PROMISE_START_CHANNEL_ID = "PromiseStartNotificationChannel"

    private fun createNotificationChannel(
        context: Context, importance: Int, showBadge: Boolean,
        name: String, description: String, channelId: String
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(channelId, name, importance)
        channel.description = description
        channel.setShowBadge(showBadge)

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun providePromiseReadyChannel(context: Context) {
        val name = context.getString(R.string.notification_channel_promise_ready)
        val description = context.getString(R.string.notification_channel_promise_ready_description)

        createNotificationChannel(
            context,
            NotificationManager.IMPORTANCE_HIGH,
            true,
            name,
            description,
            PROMISE_CHANNEL_ID
        )
    }
}