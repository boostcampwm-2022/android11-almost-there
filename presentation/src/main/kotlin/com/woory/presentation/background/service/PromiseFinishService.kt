package com.woory.presentation.background.service

import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.R
import com.woory.presentation.background.notification.NotificationChannelProvider
import com.woory.presentation.background.notification.NotificationProvider
import com.woory.presentation.background.util.asPromiseAlarm
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.ui.gameresult.GameResultActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class PromiseFinishService : LifecycleService() {

    @Inject
    lateinit var repository: PromiseRepository

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationChannelProvider.provideServiceChannel(this)
        }

        val notification = NotificationProvider.createNotificationBuilder(
            this,
            NotificationChannelProvider.PROMISE_READY_SERVICE_CHANNEL_ID,
            getString(R.string.notification_finish_promise_title),
            null,
            NotificationCompat.PRIORITY_LOW,
            null
        ).build()

        startForeground(NotificationProvider.PROMISE_READY_NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent ?: throw IllegalArgumentException("intent is null")

        val promiseAlarm = intent.asPromiseAlarm()

        lifecycleScope.launch {
            repository.setIsFinishedPromise(promiseAlarm.promiseCode)
                .onFailure {
                    Timber.tag("ERROR").d("Failure finish promise")
                }
                .also {
                    stopService(promiseAlarm)
                }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stopService(promiseAlarm: PromiseAlarm) {
        stopSelf()
        notifyFinishNotification(promiseAlarm)
    }

    private fun notifyFinishNotification(promiseAlarm: PromiseAlarm) {
        NotificationProvider.notifyActivityNotification(
            this,
            this.getString(R.string.notification_finished_title),
            this.getString(R.string.notification_finished_content),
            promiseAlarm,
            GameResultActivity::class.java
        )
    }
}