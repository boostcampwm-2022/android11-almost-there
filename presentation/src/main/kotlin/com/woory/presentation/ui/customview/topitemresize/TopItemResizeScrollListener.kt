package com.woory.presentation.ui.customview.topitemresize

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class TopItemResizeScrollListener(private val linearLayoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    private var firstVisibleItemIndex = linearLayoutManager.findFirstVisibleItemPosition()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (firstVisibleItemIndex == linearLayoutManager.findFirstVisibleItemPosition()) return
        firstVisibleItemIndex = linearLayoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemIndex = linearLayoutManager.findLastVisibleItemPosition()

        val start = if (firstVisibleItemIndex - 1 == -1) 0 else firstVisibleItemIndex - 1
        (start..lastVisibleItemIndex).forEach { index ->
            val view = linearLayoutManager.findViewByPosition(index)
            if (view != null) {
                val viewHolder = recyclerView.getChildViewHolder(view) as? TopItemResizeAdapter.HighlightAble
                if (viewHolder != null) {
                    viewHolder.setHighlight(firstVisibleItemIndex == index)
                } else {
                    Timber.tag("TopItemResizeScrollListener").d("viewHolder[$index] is not HighlightAble.")
                }
            } else {
                Timber.tag("TopItemResizeScrollListener").d("itemView[$index] is null.")
            }
        }
    }
}