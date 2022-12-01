package com.woory.presentation.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woory.presentation.databinding.ItemPromiseHistoryBinding
import com.woory.presentation.model.Promise
import org.threeten.bp.OffsetDateTime

class PromiseHistoryAdapter(
    private val onClick: (PromiseHistoryViewType?, Promise?) -> Unit
) : ListAdapter<Promise, PromiseHistoryAdapter.PromiseHistoryViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromiseHistoryViewHolder =
        PromiseHistoryViewHolder(
            ItemPromiseHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick
        )

    override fun onBindViewHolder(holder: PromiseHistoryViewHolder, position: Int) =
        holder.bind(getItem(position))

    class PromiseHistoryViewHolder(
        private val binding: ItemPromiseHistoryBinding,
        private val onClick: (PromiseHistoryViewType?, Promise?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onClick(binding.type, binding.promise)
            }
        }

        fun bind(item: Promise) = with(binding) {
            promise = item

            val currentTime = OffsetDateTime.now()
            type = if (item.data.gameDateTime > currentTime) {
                PromiseHistoryViewType.BEFORE
            } else if (item.data.promiseDateTime > currentTime) {
                PromiseHistoryViewType.ONGOING
            } else {
                PromiseHistoryViewType.END
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Promise>() {

            override fun areItemsTheSame(oldItem: Promise, newItem: Promise): Boolean =
                oldItem.code == newItem.code

            override fun areContentsTheSame(oldItem: Promise, newItem: Promise): Boolean =
                oldItem == newItem
        }
    }
}