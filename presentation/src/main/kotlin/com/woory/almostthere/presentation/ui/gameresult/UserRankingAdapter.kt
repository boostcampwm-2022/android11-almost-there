package com.woory.almostthere.presentation.ui.gameresult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.woory.almostthere.presentation.databinding.ItemRankingBinding
import com.woory.almostthere.presentation.model.user.gameresult.UserRanking
import com.woory.almostthere.presentation.ui.customview.topitemresize.TopItemResizeAdapter

class UserRankingAdapter : TopItemResizeAdapter<UserRanking, ItemRankingBinding, UserRankingAdapter.ItemViewHolder>(diffUtil) {

    class ItemViewHolder(private val binding: ItemRankingBinding): TopItemResizeAdapter.ItemViewHolder<UserRanking, ItemRankingBinding>(binding) {
        override fun bind(item: UserRanking) {
            binding.userRanking = item
        }
        override fun onHighlight(binding: ItemRankingBinding) {
            binding.isHighlight = true
        }
        override fun onNotHighlight(binding: ItemRankingBinding) {
            binding.isHighlight = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemRankingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<UserRanking>() {
            override fun areItemsTheSame(oldItem: UserRanking, newItem: UserRanking): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: UserRanking, newItem: UserRanking): Boolean {
                return oldItem == newItem
            }
        }
    }
}