package com.woory.almostthere.ui.promises

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ItemPromiseEndBinding
import com.woory.almostthere.model.PromiseInfoModel
import com.woory.almostthere.ui.BaseViewHolder

class PromisesAdapter :
    ListAdapter<PromiseInfoModel, BaseViewHolder<PromiseInfoModel, ItemPromiseEndBinding>>(
        PromiseInfoModelDiff()
    ) {

    // Todo :: viewType 별로 ViewHolder 생성 후 분기 [호현]

    class PromiseEndViewHolder(
        viewGroup: ViewGroup,
        @LayoutRes itemPromise: Int,
    ) : BaseViewHolder<PromiseInfoModel, ItemPromiseEndBinding>(viewGroup, itemPromise) {

        override val binding: ItemPromiseEndBinding = ItemPromiseEndBinding.bind(itemView)

        override fun bind(item: PromiseInfoModel) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        @LayoutRes viewType: Int
    ): BaseViewHolder<PromiseInfoModel, ItemPromiseEndBinding> {
        return PromiseEndViewHolder(parent, R.layout.item_promise_end)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<PromiseInfoModel, ItemPromiseEndBinding>,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

}

class PromiseInfoModelDiff : DiffUtil.ItemCallback<PromiseInfoModel>() {
    override fun areItemsTheSame(oldItem: PromiseInfoModel, newItem: PromiseInfoModel): Boolean {
        return oldItem.promiseLocation == newItem.promiseLocation && oldItem.promiseTime == newItem.promiseTime && oldItem.gameTime == newItem.gameTime
    }

    override fun areContentsTheSame(oldItem: PromiseInfoModel, newItem: PromiseInfoModel): Boolean {
        return oldItem == newItem
    }

}