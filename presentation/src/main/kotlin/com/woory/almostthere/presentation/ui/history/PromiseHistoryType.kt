package com.woory.almostthere.presentation.ui.history

import android.content.Context
import androidx.annotation.StringRes
import com.woory.almostthere.presentation.R

enum class PromiseHistoryType(@StringRes private val titleResId: Int) {
    PAST(R.string.past_promise_history),
    FUTURE(R.string.future_promise_history);

    fun getTitle(context: Context): String = context.getString(titleResId)
}

enum class PromiseHistoryViewType {
    BEFORE, ONGOING, END
}