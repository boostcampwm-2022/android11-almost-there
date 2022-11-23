package com.woory.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<ITEM : Any, VDB : ViewDataBinding>(
    private val parent: ViewGroup,
    @LayoutRes private val layoutRes: Int
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)) {

    abstract val binding: VDB
    abstract fun bind(item: ITEM)
}