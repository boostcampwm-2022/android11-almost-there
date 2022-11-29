package com.woory.presentation.background.alarm

import android.content.Context
import android.content.Intent
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.R
import com.woory.presentation.background.HiltBroadcastReceiver
import com.woory.presentation.background.notification.NotificationProvider
import com.woory.presentation.background.util.asPromiseAlarm
import com.woory.presentation.model.AlarmState
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.model.mapper.alarm.asDomain
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmTouchReceiver : HiltBroadcastReceiver() {

    @Inject lateinit var repository: PromiseRepository
    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        context ?: return
        intent ?: return

        val promiseAlarm = intent.asPromiseAlarm()

        when (promiseAlarm.state) {
            AlarmState.READY -> {
                onTouchPromiseReadyNotification(context, promiseAlarm)
            }
            AlarmState.START -> {

            }
            AlarmState.END -> {

            }
        }
    }

    private fun onTouchPromiseReadyNotification(
        context: Context,
        promiseAlarm: PromiseAlarm
    ) {
        coroutineScope.launch {
            val alarmFunctions = AlarmFunctions(context)
            promiseAlarm.copy(state = AlarmState.START).run {
                alarmFunctions.registerAlarm(this)
                repository.setPromiseAlarmByPromiseAlarmModel(this.asDomain())
                    .onSuccess {
                        delay(1000)
                        NotificationProvider.notifyActivityNotification(
                            context,
                            context.getString(R.string.notification_ready_complete_title),
                            context.getString(R.string.notification_ready_complete_content),
                            promiseAlarm,
                            PromiseInfoActivity::class.java,
                        )
                    }
            }
        }
    }
}