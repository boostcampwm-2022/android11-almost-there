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
import com.woory.presentation.model.mapper.alarm.asUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AlarmRestartService: LifecycleService() {

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
            getString(R.string.notification_alarm_restart),
            null,
            NotificationCompat.PRIORITY_LOW,
            null
        ).build()

        startForeground(NotificationProvider.PROMISE_READY_NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        lifecycle.coroutineScope.launch {
            repository.getAllPromiseAlarms()
                .onSuccess { promiseAlarmModels ->
                    promiseAlarmModels.forEach { promiseAlarmModel ->
                        alarmFunctions.registerAlarm(promiseAlarmModel.asUiModel())
                    }
                }
                .onFailure {
                    Timber.tag("TAG").d("Error -> $it")
                }
                .also {
                    stopSelf()
                }
        }
        return START_NOT_STICKY
    }
}