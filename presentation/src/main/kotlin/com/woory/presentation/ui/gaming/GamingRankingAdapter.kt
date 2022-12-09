package com.woory.presentation.ui.gaming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woory.presentation.databinding.ItemGamingRankingBinding
import com.woory.presentation.model.user.gameresult.UserRanking

class GamingRankingAdapter : ListAdapter<UserRanking, GamingRankingAdapter.ViewHolder>(diffUtil) {

    class ViewHolder(private val binding: ItemGamingRankingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userRanking: UserRanking) {
            binding.userRanking = userRanking
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGamingRankingBinding.inflate(
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