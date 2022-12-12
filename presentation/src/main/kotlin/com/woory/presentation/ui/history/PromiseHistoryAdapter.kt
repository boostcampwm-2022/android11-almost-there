package com.woory.presentation.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woory.presentation.databinding.ItemPromiseHistoryBinding
import com.woory.presentation.model.PromiseHistory
import org.threeten.bp.OffsetDateTime

class PromiseHistoryAdapter(
    private val onClick: (PromiseHistoryViewType?, PromiseHistory?) -> Unit
) : ListAdapter<PromiseHistory, PromiseHistoryAdapter.PromiseHistoryViewHolder>(diffUtil) {

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
        private val onClick: (PromiseHistoryViewType?, PromiseHistory?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onClick(binding.type, binding.promiseHistory)
            }
        }

        fun bind(item: PromiseHistory) = with(binding) {
            promiseHistory = item

            val currentTime = OffsetDateTime.now()
            type = if (item.promise.data.gameDateTime > currentTime) {
                PromiseHistoryViewType.BEFORE
            } else if (item.promise.data.promiseDateTime > currentTime) {
                PromiseHistoryViewType.ONGOING
            } else {
                PromiseHistoryViewType.END
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<PromiseHistory>() {

            override fun areItemsTheSame(
                oldItem: PromiseHistory,
                newItem: PromiseHistory
            ): Boolean =
                oldItem.promise.code == newItem.promise.code

            override fun areContentsTheSame(
                oldItem: PromiseHistory,
                newItem: PromiseHistory
            ): Boolean =
                oldItem == newItem
        }
    }
}