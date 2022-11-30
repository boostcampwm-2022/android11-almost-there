package com.woory.presentation.background.service

import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.coroutineScope
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.R
import com.woory.presentation.background.alarm.AlarmFunctions
import com.woory.presentation.background.notification.NotificationChannelProvider
import com.woory.presentation.background.notification.NotificationProvider
import com.woory.presentation.background.util.asPromiseAlarm
import com.woory.presentation.model.mapper.alarm.asDomain
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PromiseAlarmRegisterService : LifecycleService() {

    @Inject
    lateinit var repository: PromiseRepository
    private val alarmFunctions = AlarmFunctions(this)

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

        startForeground(NotificationProvider.PROMISE_READY_NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent ?: throw IllegalArgumentException("intent is null")

        val promiseAlarm = intent.asPromiseAlarm()

        lifecycle.coroutineScope.launch {
            repository.setPromiseAlarmByPromiseAlarmModel(promiseAlarm.asDomain())
        }

        alarmFunctions.registerAlarm(promiseAlarm)

        stopSelf()
        return START_STICKY
    }
}