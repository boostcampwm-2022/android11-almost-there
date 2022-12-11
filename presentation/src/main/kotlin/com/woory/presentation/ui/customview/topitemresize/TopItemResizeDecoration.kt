package com.woory.presentation.ui.customview.topitemresize

import android.graphics.Rect
import android.view.View
import androidx.core.view.children
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
        if (parent.getChildAdapterPosition(view) + 1 == parent.adapter?.itemCount && parent.childCount != 1) {
            when (val layoutManager = parent.layoutManager) {
                is LinearLayoutManager -> {
                    if (layoutManager.orientation == RecyclerView.VERTICAL) {
                        val highlightViewHeight = parent.children.maxOf { it.height }
                        outRect.bottom = parent.height - highlightViewHeight - parent.paddingTop - parent.paddingBottom
                    } else {
                        val highlightViewWidth = parent.children.maxOf { it.width }
                        outRect.right = parent.width - highlightViewWidth - parent.paddingStart - parent.paddingEnd
                    }
                }
                else -> throw IllegalArgumentException("TopItemResizeDecoration 은 LinearLayoutManager 일 때만 지원합니다.")
            }
        }
    }
}