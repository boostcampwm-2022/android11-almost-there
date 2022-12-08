package com.woory.presentation.binding

import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.woory.presentation.R
import com.woory.presentation.model.Color
import com.woory.presentation.model.Promise
import com.woory.presentation.ui.customview.RankBadge
import com.woory.presentation.ui.history.PromiseHistoryViewType
import com.woory.presentation.ui.history.RankBadgeType
import com.woory.presentation.util.getDip

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