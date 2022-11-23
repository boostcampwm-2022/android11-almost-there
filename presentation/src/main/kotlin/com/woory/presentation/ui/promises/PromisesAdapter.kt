package com.woory.presentation.ui.promises

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.woory.presentation.R
import com.woory.presentation.databinding.ItemPromiseBeforeBinding
import com.woory.presentation.databinding.ItemPromiseEndBinding
import com.woory.presentation.databinding.ItemPromiseOngoingBinding
import com.woory.presentation.model.PromiseDataModel
import com.woory.presentation.ui.BaseViewHolder
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset

class PromisesAdapter(
    private val onClickBefore: (PromiseDataModel?) -> Unit,
    private val onClickOngoing: (PromiseDataModel?) -> Unit,
    private val onClickEnd: (PromiseDataModel?) -> Unit
) :
    ListAdapter<PromiseDataModel, BaseViewHolder<PromiseDataModel, *>>(
        PromiseInfoModelDiff()
    ) {

    class PromiseEndViewHolder(
        viewGroup: ViewGroup,
        onClickEnd: (PromiseDataModel?) -> Unit,
        @LayoutRes itemPromise: Int,
    ) : BaseViewHolder<PromiseDataModel, ItemPromiseEndBinding>(viewGroup, itemPromise) {

        override val binding: ItemPromiseEndBinding = ItemPromiseEndBinding.bind(itemView)

        init {
            binding.root.setOnClickListener {
                binding.item?.let {
                    onClickEnd(binding.item)
                }
            }
        }

        override fun bind(item: PromiseDataModel) {
            binding.item = item
        }
    }

    class PromiseOngoingViewHolder(
        viewGroup: ViewGroup,
        onClickOngoing: (PromiseDataModel?) -> Unit,
        @LayoutRes itemPromise: Int,
    ) : BaseViewHolder<PromiseDataModel, ItemPromiseOngoingBinding>(viewGroup, itemPromise) {

        override val binding: ItemPromiseOngoingBinding = ItemPromiseOngoingBinding.bind(itemView)

        init {
            binding.root.setOnClickListener {
                binding.item?.let {
                    onClickOngoing(binding.item)
                }
            }
        }

        override fun bind(item: PromiseDataModel) {
            binding.item = item
        }
    }

    class PromiseBeforeViewHolder(
        viewGroup: ViewGroup,
        onClickBefore: (PromiseDataModel?) -> Unit,
        @LayoutRes itemPromise: Int,
    ) : BaseViewHolder<PromiseDataModel, ItemPromiseBeforeBinding>(viewGroup, itemPromise) {

        override val binding: ItemPromiseBeforeBinding = ItemPromiseBeforeBinding.bind(itemView)

        init {
            binding.root.setOnClickListener {
                binding.item?.let {
                    onClickBefore(binding.item)
                }
            }
        }

        override fun bind(item: PromiseDataModel) {
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
    ): BaseViewHolder<PromiseDataModel, *> {
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
        holder: BaseViewHolder<PromiseDataModel, *>,
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

class PromiseInfoModelDiff : DiffUtil.ItemCallback<PromiseDataModel>() {
    override fun areItemsTheSame(oldItem: PromiseDataModel, newItem: PromiseDataModel): Boolean {
        return oldItem.promiseLocation == newItem.promiseLocation && oldItem.promiseDateTime == newItem.promiseDateTime && oldItem.gameDateTime == newItem.gameDateTime
    }

    override fun areContentsTheSame(oldItem: PromiseDataModel, newItem: PromiseDataModel): Boolean {
        return oldItem == newItem
    }

}