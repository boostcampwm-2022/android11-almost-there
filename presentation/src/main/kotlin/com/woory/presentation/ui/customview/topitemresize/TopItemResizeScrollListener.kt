package com.woory.presentation.ui.customview.topitemresize

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class TopItemResizeScrollListener(private val linearLayoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    private var firstVisibleItemIndex = -1

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (firstVisibleItemIndex == linearLayoutManager.findFirstCompletelyVisibleItemPosition()) return
        firstVisibleItemIndex = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
        val lastVisibleItemIndex = linearLayoutManager.findLastVisibleItemPosition()

        val start = when {
            firstVisibleItemIndex - 1 == -1 -> 0
            firstVisibleItemIndex == lastVisibleItemIndex -> firstVisibleItemIndex
            else -> firstVisibleItemIndex - 1
        }

        val highlightIndex = when (firstVisibleItemIndex) {
            -1 -> lastVisibleItemIndex
            else -> firstVisibleItemIndex
        }

        (start..lastVisibleItemIndex).forEach { index ->
            val view = linearLayoutManager.findViewByPosition(index)
            if (view != null) {
                val viewHolder = recyclerView.getChildViewHolder(view) as? TopItemResizeAdapter.HighlightAble
                if (viewHolder != null) {
                    viewHolder.setHighlight(highlightIndex == index)
                } else {
                    Timber.tag("ScrollChangeListener")
                        .d("%s is not HighlightAble.", "viewHolder($index)")
                }
            } else {
                Timber.tag("ScrollChangeListener").d("itemView($index) is null.")
            }
        }
    }
}