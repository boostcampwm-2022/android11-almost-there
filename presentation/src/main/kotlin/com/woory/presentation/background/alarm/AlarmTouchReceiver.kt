package com.woory.presentation.background.alarm

import android.content.Context
import android.content.Intent
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.background.HiltBroadcastReceiver
import com.woory.presentation.background.util.asPromiseAlarm
import com.woory.presentation.model.AlarmState
import com.woory.presentation.model.PromiseAlarm
import com.woory.presentation.model.mapper.alarm.asDomain
import com.woory.presentation.ui.promiseinfo.PromiseInfoActivity
import com.woory.presentation.util.PROMISE_CODE_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmTouchReceiver : HiltBroadcastReceiver() {

    @Inject lateinit var repository: PromiseRepository
    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        context ?: throw IllegalArgumentException("is context null")
        intent ?: throw IllegalArgumentException("is intent null")

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
            }
        }

        val intent = Intent(context, PromiseInfoActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(PROMISE_CODE_KEY, promiseAlarm.promiseCode)
        }

        context.startActivity(intent)
    }
}