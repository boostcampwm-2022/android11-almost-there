package com.woory.presentation.util

import android.content.Context
import com.woory.presentation.R
import com.woory.presentation.model.exception.InvalidForCreatingPromiseDataEmpty
import com.woory.presentation.model.exception.InvalidGameTimeException

fun getExceptionMessage(context: Context, throwable: Throwable): String {
    return when (throwable) {
        is InvalidGameTimeException -> context.getString(R.string.invalid_game_date_time_message)
        is InvalidForCreatingPromiseDataEmpty -> context.getString(R.string.invalid_for_creating_promise_data_empty)
        else -> String.format(context.getString(R.string.unknown_error), throwable.message)
    }
}