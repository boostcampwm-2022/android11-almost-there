package com.woory.almostthere.presentation.ui.promiseinfo

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.databinding.ItemPromiseUserBinding
import com.woory.almostthere.presentation.model.ReadyUser
import com.woory.almostthere.presentation.ui.BaseViewHolder

class PromiseUserAdapter(
    private val viewModel: PromiseInfoViewModel
) : ListAdapter<ReadyUser, PromiseUserAdapter.PromiseUserViewHolder>(
    PromiseUserDiff()
) {
    inner class PromiseUserViewHolder(
        viewGroup: ViewGroup,
        @LayoutRes item: Int
    ) : BaseViewHolder<ReadyUser, ItemPromiseUserBinding>(viewGroup, item) {
        override val binding: ItemPromiseUserBinding = ItemPromiseUserBinding.bind(itemView)

        override fun bind(item: ReadyUser) {
            binding.ivHost.visibility =
                if (viewModel.isHost(item.user.userId)) View.VISIBLE else View.GONE
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromiseUserViewHolder =
        PromiseUserViewHolder(
            parent,
            R.layout.item_promise_user
        )

    override fun onBindViewHolder(holder: PromiseUserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PromiseUserDiff : DiffUtil.ItemCallback<ReadyUser>() {
    override fun areItemsTheSame(oldItem: ReadyUser, newItem: ReadyUser): Boolean {
        return oldItem.user.userId == newItem.user.userId
    }

    override fun areContentsTheSame(oldItem: ReadyUser, newItem: ReadyUser): Boolean {
        return oldItem == newItem
    }

}