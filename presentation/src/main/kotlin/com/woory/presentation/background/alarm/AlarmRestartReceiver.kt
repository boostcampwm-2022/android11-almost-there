package com.woory.presentation.background.alarm

import android.content.Context
import android.content.Intent
import com.woory.data.repository.PromiseRepository
import com.woory.presentation.background.HiltBroadcastReceiver
import com.woory.presentation.model.mapper.alarm.asUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AlarmRestartReceiver : HiltBroadcastReceiver() {

    @Inject
    lateinit var repository: PromiseRepository
    private val coroutineScope by lazy { CoroutineScope(Dispatchers.IO) }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        context ?: throw IllegalArgumentException("is context null")
        intent ?: throw IllegalArgumentException("is intent null")


        if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            val alarmFunctions = AlarmFunctions(context)
            coroutineScope.launch {
                repository.getAllPromiseAlarms()
                    .onSuccess { promiseAlarmModels ->
                        promiseAlarmModels.forEach { promiseAlarmModel ->
                            alarmFunctions.registerAlarm(promiseAlarmModel.asUiModel())
                        }
                    }
                    .onFailure {
                        Timber.tag("TAG").d("Error -> $it")
                    }
            }
        }
    }
}