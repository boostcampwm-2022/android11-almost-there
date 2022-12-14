package com.woory.almostthere.presentation.binding

import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.model.Promise
import com.woory.almostthere.presentation.ui.history.PromiseHistoryViewType
import com.woory.almostthere.presentation.util.TimeUtils.getDurationStringInMinuteToDay
import org.threeten.bp.OffsetDateTime

@BindingAdapter(value = ["itemStateType", "itemStatePromise"], requireAll = true)
fun AppCompatTextView.bindItemState(type: PromiseHistoryViewType, promise: Promise) {
    val currentDateTime = OffsetDateTime.now()
    val beforeStartTime =
        getDurationStringInMinuteToDay(context, currentDateTime, promise.data.gameDateTime)
    val beforeEndTime =
        getDurationStringInMinuteToDay(context, currentDateTime, promise.data.promiseDateTime)

    text = when (type) {
        PromiseHistoryViewType.BEFORE -> context.getString(
            R.string.history_item_state_before,
            beforeStartTime
        )
        PromiseHistoryViewType.ONGOING -> context.getString(
            R.string.history_item_state_ongoing,
            beforeEndTime
        )
        PromiseHistoryViewType.END -> context.getString(R.string.promises_end)
    }
}

@BindingAdapter("date_time")
fun AppCompatTextView.bindDateTime(dateTime: OffsetDateTime) = with(dateTime) {
    text = context.getString(
        R.string.date_time_template,
        year,
        monthValue,
        dayOfMonth,
        hour,
        minute
    )
}

@BindingAdapter("textSize")
fun TextView.bindTextSize(size: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, size.toFloat())
}