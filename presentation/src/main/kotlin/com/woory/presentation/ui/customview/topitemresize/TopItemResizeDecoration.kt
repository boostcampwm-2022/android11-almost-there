package com.woory.presentation.ui.customview.topitemresize

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TopItemResizeDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) + 1 == parent.adapter?.itemCount) {
            when (val layoutManager = parent.layoutManager) {
                is LinearLayoutManager -> {
                    if (layoutManager.orientation == RecyclerView.VERTICAL) {
                        outRect.bottom = parent.height - view.height
                    } else {
                        outRect.right = parent.width - view.width
                    }
                }
                else -> throw IllegalArgumentException("TopItemResizeDecoration 은 LinearLayoutManager 일 때만 지원합니다.")
            }
        }
    }
}