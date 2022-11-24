package com.woory.presentation.ui.promises

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.woory.presentation.R
import com.woory.presentation.databinding.ItemPromiseBeforeBinding
import com.woory.presentation.databinding.ItemPromiseEndBinding
import com.woory.presentation.databinding.ItemPromiseOngoingBinding
import com.woory.presentation.model.Promise
import com.woory.presentation.ui.BaseViewHolder
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset

class PromisesAdapter(
    private val onClickBefore: (Promise?) -> Unit,
    private val onClickOngoing: (Promise?) -> Unit,
    private val onClickEnd: (Promise?) -> Unit
) :
    ListAdapter<Promise, BaseViewHolder<Promise, *>>(
        PromiseInfoModelDiff()
    ) {

    class PromiseEndViewHolder(
        viewGroup: ViewGroup,
        onClickEnd: (Promise?) -> Unit,
        @LayoutRes itemPromise: Int,
    ) : BaseViewHolder<Promise, ItemPromiseEndBinding>(viewGroup, itemPromise) {

        override val binding: ItemPromiseEndBinding = ItemPromiseEndBinding.bind(itemView)

        init {
            binding.root.setOnClickListener {
                binding.item?.let {
                    onClickEnd(binding.item)
                }
            }
        }

        override fun bind(item: Promise) {
            binding.item = item
        }
    }

    class PromiseOngoingViewHolder(
        viewGroup: ViewGroup,
        onClickOngoing: (Promise?) -> Unit,
        @LayoutRes itemPromise: Int,
    ) : BaseViewHolder<Promise, ItemPromiseOngoingBinding>(viewGroup, itemPromise) {

        override val binding: ItemPromiseOngoingBinding = ItemPromiseOngoingBinding.bind(itemView)

        init {
            binding.root.setOnClickListener {
                binding.item?.let {
                    onClickOngoing(binding.item)
                }
            }
        }

        override fun bind(item: Promise) {
            binding.item = item
        }
    }

    class PromiseBeforeViewHolder(
        viewGroup: ViewGroup,
        onClickBefore: (Promise?) -> Unit,
        @LayoutRes itemPromise: Int,
    ) : BaseViewHolder<Promise, ItemPromiseBeforeBinding>(viewGroup, itemPromise) {

        override val binding: ItemPromiseBeforeBinding = ItemPromiseBeforeBinding.bind(itemView)

        init {
            binding.root.setOnClickListener {
                binding.item?.let {
                    onClickBefore(binding.item)
                }
            }
        }

        override fun bind(item: Promise) {
            binding.item = item
        }
    }

    override fun getItemViewType(position: Int): Int {
        // Todo :: 삭제 해야하는 더미 데이터
        val dummyCurrentDate =
            OffsetDateTime.of(2022, 11, 20, 12, 20, 0, 0, ZoneOffset.of("+09:00"))

        val promise = getItem(position)

        return when {
            promise.data.gameDateTime > dummyCurrentDate -> PROMISE_BEFORE
            promise.data.promiseDateTime > dummyCurrentDate -> PROMISE_ONGOING
            else -> PROMISE_END
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Promise, *> {
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
        holder: BaseViewHolder<Promise, *>,
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

class PromiseInfoModelDiff : DiffUtil.ItemCallback<Promise>() {
    override fun areItemsTheSame(oldItem: Promise, newItem: Promise): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: Promise, newItem: Promise): Boolean {
        return oldItem == newItem
    }

}