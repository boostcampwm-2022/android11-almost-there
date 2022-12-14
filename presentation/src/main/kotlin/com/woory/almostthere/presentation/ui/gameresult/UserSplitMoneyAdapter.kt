package com.woory.almostthere.presentation.ui.gameresult

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.databinding.ItemBalanceBinding
import com.woory.almostthere.presentation.databinding.ItemUserSplitMoneyBinding
import com.woory.almostthere.presentation.model.user.gameresult.UserSplitMoneyItem
import com.woory.almostthere.presentation.ui.BaseViewHolder

class UserSplitMoneyAdapter :
    ListAdapter<UserSplitMoneyItem, BaseViewHolder<UserSplitMoneyItem, *>>(diffUtil) {

    class UserSplitMoneyViewHolder(viewGroup: ViewGroup, @LayoutRes itemUserPayment: Int) :
        BaseViewHolder<UserSplitMoneyItem, ItemUserSplitMoneyBinding>(viewGroup, itemUserPayment) {

        override val binding: ItemUserSplitMoneyBinding = ItemUserSplitMoneyBinding.bind(itemView)

        override fun bind(item: UserSplitMoneyItem) {
            binding.userSplitMoney = item as UserSplitMoneyItem.UserSplitMoney
        }
    }

    class BalanceViewHolder(viewGroup: ViewGroup, @LayoutRes itemBalance: Int) :
        BaseViewHolder<UserSplitMoneyItem, ItemBalanceBinding>(viewGroup, itemBalance) {

        override val binding: ItemBalanceBinding = ItemBalanceBinding.bind(itemView)

        override fun bind(item: UserSplitMoneyItem) {
            binding.balance = item as UserSplitMoneyItem.Balance
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<UserSplitMoneyItem, *> {
        return when (viewType) {
            USER_SPLIT_MONEY_SECTION -> UserSplitMoneyViewHolder(parent, R.layout.item_user_split_money)
            FOOTER_SECTION -> BalanceViewHolder(parent, R.layout.item_balance)
            else -> throw IllegalArgumentException("UserPaymentAdapter: Unknown ViewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<UserSplitMoneyItem, *>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is UserSplitMoneyItem.UserSplitMoney -> USER_SPLIT_MONEY_SECTION
            is UserSplitMoneyItem.Balance -> FOOTER_SECTION
        }
    }

    companion object {
        private const val USER_SPLIT_MONEY_SECTION = 0
        private const val FOOTER_SECTION = 1

        val diffUtil = object : DiffUtil.ItemCallback<UserSplitMoneyItem>() {
            override fun areItemsTheSame(
                oldItem: UserSplitMoneyItem,
                newItem: UserSplitMoneyItem
            ): Boolean {
                return if (oldItem is UserSplitMoneyItem.UserSplitMoney && newItem is UserSplitMoneyItem.UserSplitMoney) {
                    oldItem.userId == newItem.userId
                } else if (oldItem is UserSplitMoneyItem.Balance && newItem is UserSplitMoneyItem.Balance) {
                    oldItem.value == newItem.value
                } else {
                    false
                }
            }

            override fun areContentsTheSame(
                oldItem: UserSplitMoneyItem,
                newItem: UserSplitMoneyItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}