package com.woory.presentation.binding

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.databinding.BindingAdapter
import com.woory.presentation.ui.history.PromiseHistoryViewType

@BindingAdapter("toGone")
fun View.bindToGone(isGone: Boolean) {
    visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("itemRankLabel")
fun View.bindItemRankLabel(type: PromiseHistoryViewType) {
    visibility = when (type) {
        PromiseHistoryViewType.BEFORE -> View.GONE
        else -> View.VISIBLE
    }
}

@BindingAdapter("itemHP")
fun View.bindItemHP(type: PromiseHistoryViewType) {
    visibility = when (type) {
        PromiseHistoryViewType.ONGOING -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("visibilityByType")
fun View.bindVisibilityByType(type: PromiseHistoryViewType) {
    visibility = when (type) {
        PromiseHistoryViewType.ONGOING -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("layoutMarginHorizontal")
fun View.bindMarinHorizontal(dimen: Float) {
    layoutParams = (layoutParams as MarginLayoutParams).apply {
        marginStart = dimen.toInt()
        marginEnd = dimen.toInt()
    }
}

@BindingAdapter("layoutMarginVertical")
fun View.bindMarinVertical(dimen: Float) {
    layoutParams = (layoutParams as MarginLayoutParams).apply {
        topMargin = dimen.toInt()
        bottomMargin = dimen.toInt()
    }
}