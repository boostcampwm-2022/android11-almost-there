package com.woory.presentation.ui.creatingpromise.locationsearch

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentLocationSearchResultBinding
import com.woory.presentation.model.Location
import com.woory.presentation.ui.BaseFragment
import kotlinx.coroutines.launch

class LocationSearchResultFragment :
    BaseFragment<FragmentLocationSearchResultBinding>(R.layout.fragment_location_search_result),
    SearchView.OnQueryTextListener {

    private val viewModel: CreatingPromiseViewModel by activityViewModels()

    private val locationSearchResultAdapter by lazy {
        LocationSearchResultAdapter {
            setSearchedLocation(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.svPromiseLocation.isIconified = false
        binding.svPromiseLocation.setOnQueryTextListener(this)

        binding.rvSearchResult.adapter = locationSearchResultAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.locationSearchResult.collect {
                    locationSearchResultAdapter.submitList(it)
                }
            }
        }

        binding.btnSubmitSearch.setOnClickListener {
            findLocation(binding.svPromiseLocation.query.toString())
        }
    }

    private fun setSearchedLocation(location: LocationSearchResult) {
        viewModel.setChoosedLocation(
            Location(location.location, location.address)
        )
        findNavController().popBackStack()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setSearchedResult(listOf())
        }
    }

    private fun findLocation(query: String) {
        viewModel.searchLocation(query)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        findLocation(query ?: DEFAULT_QUERY)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean = false

    companion object {
        private const val DEFAULT_QUERY = ""
    }
}