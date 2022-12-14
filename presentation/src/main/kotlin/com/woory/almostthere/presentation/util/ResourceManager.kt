package com.woory.almostthere.presentation.util

import android.content.Context
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.model.exception.InvalidForCreatingPromiseDataEmpty
import com.woory.almostthere.presentation.model.exception.InvalidGameTimeException
import com.woory.almostthere.presentation.model.exception.NotFoundSearchResult

fun getExceptionMessage(context: Context, throwable: Throwable): String {
    return when (throwable) {
        is InvalidGameTimeException -> context.getString(R.string.invalid_game_date_time_message)
        is InvalidForCreatingPromiseDataEmpty -> context.getString(R.string.invalid_for_creating_promise_data_empty)
        is NotFoundSearchResult -> context.getString(R.string.not_found_search_result_message)
        else -> String.format(context.getString(R.string.unknown_error), throwable.message)
    }
}