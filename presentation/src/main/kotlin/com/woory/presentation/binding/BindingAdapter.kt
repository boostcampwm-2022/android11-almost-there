package com.woory.presentation.binding

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputLayout
import com.woory.presentation.R
import com.woory.presentation.model.Color
import com.woory.presentation.model.ProfileImage
import com.woory.presentation.model.Promise
import com.woory.presentation.ui.customview.RankBadge
import com.woory.presentation.ui.history.PromiseHistoryViewType
import com.woory.presentation.ui.history.RankBadgeType
import com.woory.presentation.ui.join.FormState
import com.woory.presentation.util.getDip
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime
import com.woory.presentation.ui.join.FormState

@BindingAdapter("state_message")
fun TextInputLayout.bindMessage(state: FormState) {
    error = if (state is FormState.Invalid) {
        context.getString(R.string.invalid_invite_code)
    } else {
        ""
    }

    helperText = if (state is FormState.Valid) {
        context.getString(R.string.valid_invite_code)
    } else {
        ""
    }
}

@BindingAdapter("enabled")
fun Button.bindEnabled(state: FormState) {
    isEnabled = state is FormState.Valid
}

@BindingAdapter("visibility")
fun ProgressBar.bindVisibility(isLoading: Boolean) {
    visibility = if (isLoading) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("backgroundColor")
fun MaterialCardView.bindBackgroundColor(color: Color) {
    setCardBackgroundColor(android.graphics.Color.parseColor(color.toString()))
}

@BindingAdapter("src")
fun ImageView.bindImage(index: Int) {
    val imageDrawable = ProfileImage.values()[index].getDrawableImage(context)

    setImageDrawable(imageDrawable)
}

@BindingAdapter("adapter")
fun RecyclerView.bindAdapter(adapter: RecyclerView.Adapter<*>) {
    this.adapter = adapter.apply {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }
}

@BindingAdapter("textVisibility")
fun AppCompatTextView.bindVisibility(isGone: Boolean) {
    visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("itemBackgroundColor")
fun MaterialCardView.bindItemBackgroundColor(type: PromiseHistoryViewType) {
    val color = when (type) {
        PromiseHistoryViewType.BEFORE -> context.getColor(R.color.almost_there_white)
        PromiseHistoryViewType.ONGOING -> context.getColor(R.color.almost_there_white)
        PromiseHistoryViewType.END -> context.getColor(R.color.almost_there_grey03)
    }

    setCardBackgroundColor(color)
}

@BindingAdapter("itemRankLabel")
fun View.bindItemRankLabel(type: PromiseHistoryViewType) {
    visibility = when (type) {
        PromiseHistoryViewType.BEFORE -> View.GONE
        else -> View.VISIBLE
    }
}

@BindingAdapter(value = ["itemStateType", "itemStatePromise"], requireAll = true)
fun AppCompatTextView.bindItemState(type: PromiseHistoryViewType, promise: Promise) {
    val currentTime = OffsetDateTime.now()
    val beforeStartTime = Duration.between(promise.data.gameDateTime, currentTime).toMinutes()
    val beforeEndTime = Duration.between(currentTime, promise.data.promiseDateTime).toMinutes()

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

@BindingAdapter("itemHP")
fun View.bindItemHP(type: PromiseHistoryViewType) {
    visibility = when (type) {
        PromiseHistoryViewType.ONGOING -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter(value = ["itemRankType", "itemRankPromise"], requireAll = true)
fun LinearLayout.bindRank(type: PromiseHistoryViewType, promise: Promise) {
    this.removeAllViews()

    visibility = when (type) {
        PromiseHistoryViewType.BEFORE -> {
            View.GONE
            return
        }
        else -> View.VISIBLE
    }

    val rankTypes = RankBadgeType.values()

    val userCounts = promise.data.users.size
    val maxRankCounts = rankTypes.size
    val rankCounts = if (userCounts < maxRankCounts) {
        userCounts
    } else {
        maxRankCounts
    }

    repeat(rankCounts) { rank ->
        val badge = RankBadge(context).apply {
            badgeColor = rankTypes[rank].colorResId
            setText(rankTypes[rank].labelResId)
            layoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    rightMargin = getDip(4f)
                }
        }

        // FIXME: HP순 내림차순 정렬
        val currentRankUserNicknameTextView = AppCompatTextView(context).apply {
            text = promise.data.users[rank].data.name
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                rightMargin = getDip(4f)
            }
        }

        addView(badge)
        addView(currentRankUserNicknameTextView)
    }
}