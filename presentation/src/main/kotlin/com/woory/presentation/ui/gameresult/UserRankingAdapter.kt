package com.woory.presentation.ui.gameresult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woory.presentation.databinding.ItemRankingBinding
import com.woory.presentation.model.user.gameresult.UserRanking

class UserRankingAdapter : ListAdapter<UserRanking, UserRankingAdapter.ViewHolder>(diffUtil) {

    class ViewHolder(private val binding: ItemRankingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserRanking) {
            binding.userRanking = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRankingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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