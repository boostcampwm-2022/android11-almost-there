package com.woory.presentation.ui.gameresult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woory.presentation.databinding.ItemUserPaymentBinding
import com.woory.presentation.model.UserPayment

class UserPaymentAdapter : ListAdapter<UserPayment, UserPaymentAdapter.ViewHolder>(diffUtil) {

    class ViewHolder(private val binding: ItemUserPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserPayment) {
            binding.userPayment = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserPaymentBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<UserPayment>() {
            override fun areItemsTheSame(oldItem: UserPayment, newItem: UserPayment): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: UserPayment, newItem: UserPayment): Boolean {
                return oldItem == newItem
            }
        }
    }
}