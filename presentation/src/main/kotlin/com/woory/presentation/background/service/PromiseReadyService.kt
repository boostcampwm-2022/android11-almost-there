package com.woory.presentation.background.service

import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.woory.presentation.R
import com.woory.presentation.background.notification.NotificationChannelProvider
import com.woory.presentation.background.notification.NotificationProvider
import com.woory.presentation.background.util.asPromiseAlarm
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity

class PromiseReadyService: LifecycleService() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationChannelProvider.provideServiceChannel(this)
        }

        val notification = NotificationProvider.createNotificationBuilder(
            this,
            NotificationChannelProvider.PROMISE_READY_SERVICE_CHANNEL_ID,
            getString(R.string.notification_ready_progress_title),
            null,
            NotificationCompat.PRIORITY_LOW,
            null
        ).build()

        startForeground(83, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        intent?.let {
            val promiseAlarm = it.asPromiseAlarm()

            // Todo :: 초기 위치 수집 + 소요시간 요청 로직 추가 해야함

            notifyReadyCompleteNotification(promiseAlarm)
        }

        stopSelf()
        return START_NOT_STICKY
    }

    private fun notifyReadyCompleteNotification(promiseAlarm: PromiseAlarm) {
        NotificationProvider.notifyActivityNotification(
            this,
            this.getString(R.string.notification_ready_complete_title),
            this.getString(R.string.notification_ready_complete_content),
            promiseAlarm,
            PromiseInfoActivity::class.java
        )
    }
}