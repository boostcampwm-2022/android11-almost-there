package com.woory.presentation.ui.promiseinfo

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.woory.presentation.R
import com.woory.presentation.databinding.ItemPromiseUserBinding
import com.woory.presentation.model.User
import com.woory.presentation.ui.BaseViewHolder

class PromiseUserAdapter(
    private val viewModel: PromiseInfoViewModel
) : ListAdapter<User, PromiseUserAdapter.PromiseUserViewHolder>(
    PromiseUserDiff()
) {
    inner class PromiseUserViewHolder(
        viewGroup: ViewGroup,
        @LayoutRes item: Int
    ) : BaseViewHolder<User, ItemPromiseUserBinding>(viewGroup, item) {
        override val binding: ItemPromiseUserBinding = ItemPromiseUserBinding.bind(itemView)

        override fun bind(item: User) {
            binding.ivHost.visibility =
                if (viewModel.isHost(item.userId)) View.VISIBLE else View.GONE
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

class PromiseUserDiff : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

}