package com.woory.presentation.background.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.woory.presentation.background.service.AlarmRestartService
import com.woory.presentation.background.util.startServiceBp

class BootBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return

        if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            val restartIntent = Intent(context, AlarmRestartService::class.java)
            context.startServiceBp(restartIntent)
        }
    }
}