package com.woory.almostthere.presentation.binding

import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.model.Color
import com.woory.almostthere.presentation.model.PromiseHistory
import com.woory.almostthere.presentation.ui.customview.HPBar
import com.woory.almostthere.presentation.ui.customview.RankBadge
import com.woory.almostthere.presentation.ui.history.PromiseHistoryViewType
import com.woory.almostthere.presentation.ui.history.RankBadgeType
import com.woory.almostthere.presentation.util.getDip

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

@BindingAdapter("hp")
fun HPBar.bindHP(hp: Int) {
    value = hp
}

@BindingAdapter(value = ["itemRankType", "itemRankPromise"], requireAll = true)
fun LinearLayout.bindRank(type: PromiseHistoryViewType, promiseHistory: PromiseHistory) {
    this.removeAllViews()

    visibility = when (type) {
        PromiseHistoryViewType.BEFORE -> {
            View.GONE
            return
        }
        else -> View.VISIBLE
    }

    promiseHistory.users ?: return

    val rankTypes = RankBadgeType.values()

    val userCounts = promiseHistory.users.size
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

        val currentRankUserNicknameTextView = AppCompatTextView(context).apply {
            text = promiseHistory.ranking?.get(rank)
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

@BindingAdapter("progress")
fun AppCompatSeekBar.bindProgress(promiseHistory: PromiseHistory) {
    val initialRadius = promiseHistory.magnetic?.initialRadius ?: 0.0
    val currentRadius = promiseHistory.magnetic?.radius ?: 0.0

    progress = ((initialRadius - currentRadius) / initialRadius * 100).toInt()
}