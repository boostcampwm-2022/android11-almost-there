package com.woory.presentation.ui.creatingpromise

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.woory.presentation.R
import com.woory.presentation.databinding.ItemLocationSearchResultBinding
import com.woory.presentation.ui.BaseViewHolder

class LocationSearchResultAdapter(
    val callback: (LocationSearchResult) -> Unit
): ListAdapter<LocationSearchResult, LocationSearchResultAdapter.LocationSearchResultViewHolder>(
    LocationResultDiff()
) {
    class LocationSearchResultViewHolder(
        viewGroup: ViewGroup,
        callback: (LocationSearchResult) -> Unit,
        @LayoutRes item: Int,
    ) : BaseViewHolder<LocationSearchResult, ItemLocationSearchResultBinding>(viewGroup, item) {
        override val binding: ItemLocationSearchResultBinding =
            ItemLocationSearchResultBinding.bind(
                itemView
            )

        init {
            binding.root.setOnClickListener {
                binding.item?.let {
                    callback(it)
                }
            }
        }

        override fun bind(item: LocationSearchResult) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationSearchResultViewHolder = LocationSearchResultViewHolder(
        parent,
        callback,
        R.layout.item_location_search_result
    )

    override fun onBindViewHolder(holder: LocationSearchResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class LocationResultDiff : DiffUtil.ItemCallback<LocationSearchResult>() {
    override fun areItemsTheSame(
        oldItem: LocationSearchResult,
        newItem: LocationSearchResult
    ): Boolean {
        return oldItem.location == newItem.location
    }

    override fun areContentsTheSame(
        oldItem: LocationSearchResult,
        newItem: LocationSearchResult
    ): Boolean {
        return oldItem == newItem
    }

}