package com.woory.almostthere.ui.promises

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.woory.almostthere.R
import com.woory.almostthere.databinding.ItemPromiseBeforeBinding
import com.woory.almostthere.databinding.ItemPromiseEndBinding
import com.woory.almostthere.databinding.ItemPromiseOngoingBinding
import com.woory.almostthere.model.PromiseInfoModel
import com.woory.almostthere.ui.BaseViewHolder
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset

class PromisesAdapter(
    private val onClickBefore: (PromiseInfoModel?) -> Unit,
    private val onClickOngoing: (PromiseInfoModel?) -> Unit,
    private val onClickEnd: (PromiseInfoModel?) -> Unit
) :
    ListAdapter<PromiseInfoModel, BaseViewHolder<PromiseInfoModel, *>>(
        PromiseInfoModelDiff()
    ) {

    class PromiseEndViewHolder(
        viewGroup: ViewGroup,
        onClickEnd: (PromiseInfoModel?) -> Unit,
        @LayoutRes itemPromise: Int,
    ) : BaseViewHolder<PromiseInfoModel, ItemPromiseEndBinding>(viewGroup, itemPromise) {

        override val binding: ItemPromiseEndBinding = ItemPromiseEndBinding.bind(itemView)

        init {
            binding.root.setOnClickListener {
                binding.item?.let {
                    onClickEnd(binding.item)
                }
            }
        }

        override fun bind(item: PromiseInfoModel) {
            binding.item = item
        }
    }

    class PromiseOngoingViewHolder(
        viewGroup: ViewGroup,
        onClickOngoing: (PromiseInfoModel?) -> Unit,
        @LayoutRes itemPromise: Int,
    ) : BaseViewHolder<PromiseInfoModel, ItemPromiseOngoingBinding>(viewGroup, itemPromise) {

        override val binding: ItemPromiseOngoingBinding = ItemPromiseOngoingBinding.bind(itemView)

        init {
            binding.root.setOnClickListener {
                binding.item?.let {
                    onClickOngoing(binding.item)
                }
            }
        }

        override fun bind(item: PromiseInfoModel) {
            binding.item = item
        }
    }

    class PromiseBeforeViewHolder(
        viewGroup: ViewGroup,
        onClickBefore: (PromiseInfoModel?) -> Unit,
        @LayoutRes itemPromise: Int,
    ) : BaseViewHolder<PromiseInfoModel, ItemPromiseBeforeBinding>(viewGroup, itemPromise) {

        override val binding: ItemPromiseBeforeBinding = ItemPromiseBeforeBinding.bind(itemView)

        init {
            binding.root.setOnClickListener {
                binding.item?.let {
                    onClickBefore(binding.item)
                }
            }
        }

        override fun bind(item: PromiseInfoModel) {
            binding.item = item
        }
    }

    override fun getItemViewType(position: Int): Int {
        // Todo :: 삭제 해야하는 더미 데이터
        val dummyCurrentDate =
            OffsetDateTime.of(2022, 11, 20, 12, 20, 0, 0, ZoneOffset.of("+09:00"))

        val promise = getItem(position)

        return when {
            promise.gameDateTime > dummyCurrentDate -> PROMISE_BEFORE
            promise.promiseDateTime > dummyCurrentDate -> PROMISE_ONGOING
            else -> PROMISE_END
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<PromiseInfoModel, *> {
        return when (viewType) {
            PROMISE_BEFORE -> PromiseBeforeViewHolder(
                parent,
                onClickBefore,
                R.layout.item_promise_before
            )
            PROMISE_ONGOING -> PromiseOngoingViewHolder(
                parent,
                onClickOngoing,
                R.layout.item_promise_ongoing
            )
            PROMISE_END -> PromiseEndViewHolder(
                parent,
                onClickEnd,
                R.layout.item_promise_end
            )
            else -> throw IllegalArgumentException("is not exist viewHolder type")
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<PromiseInfoModel, *>,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    companion object {
        const val PROMISE_BEFORE = 0
        const val PROMISE_ONGOING = 1
        const val PROMISE_END = 2
    }

}

class PromiseInfoModelDiff : DiffUtil.ItemCallback<PromiseInfoModel>() {
    override fun areItemsTheSame(oldItem: PromiseInfoModel, newItem: PromiseInfoModel): Boolean {
        return oldItem.promiseLocation == newItem.promiseLocation && oldItem.promiseDateTime == newItem.promiseDateTime && oldItem.gameDateTime == newItem.gameDateTime
    }

    override fun areContentsTheSame(oldItem: PromiseInfoModel, newItem: PromiseInfoModel): Boolean {
        return oldItem == newItem
    }

}