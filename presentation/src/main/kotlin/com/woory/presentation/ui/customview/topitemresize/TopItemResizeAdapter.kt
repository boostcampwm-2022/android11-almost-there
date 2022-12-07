package com.woory.presentation.ui.customview.topitemresize

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class TopItemResizeAdapter<T : Any, VDB : ViewDataBinding, VH : TopItemResizeAdapter.ItemViewHolder<T, VDB>>(
    diffCallback: DiffUtil.ItemCallback<T>
) :
    ListAdapter<T, VH>(diffCallback) {

    interface HighlightAble {
        fun setHighlight(value: Boolean)
    }

    abstract class ItemViewHolder<T : Any, VDB : ViewDataBinding>(private val binding: VDB) :
        RecyclerView.ViewHolder(binding.root), HighlightAble {
        override fun setHighlight(value: Boolean) {
            if (value) {
                onHighlight(binding)
            } else {
                onNotHighlight(binding)
            }
        }

        abstract fun bind(item: T)
        abstract fun onHighlight(binding: VDB)
        abstract fun onNotHighlight(binding: VDB)
    }
}