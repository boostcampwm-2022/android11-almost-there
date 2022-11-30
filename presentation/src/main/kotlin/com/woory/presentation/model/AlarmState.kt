package com.woory.presentation.model

enum class AlarmState(val current: String) {
    READY("READY"), START("START"), END("END")
}

fun String.asAlarmState() = when (this) {
    "READY" -> AlarmState.READY
    "START" -> AlarmState.START
    "END" -> AlarmState.END
    else -> throw IllegalArgumentException()
}