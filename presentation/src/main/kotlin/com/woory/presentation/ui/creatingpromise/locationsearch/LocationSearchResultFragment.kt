package com.woory.presentation.ui.creatingpromise.locationsearch

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentLocationSearchResultBinding
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.ui.creatingpromise.CreatingPromiseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationSearchResultFragment :
    BaseFragment<FragmentLocationSearchResultBinding>(R.layout.fragment_location_search_result) {

    private val activityViewModel: CreatingPromiseViewModel by activityViewModels()
    private val fragmentViewModel: LocationSearchResultViewModel by viewModels()

    private val locationSearchResultAdapter by lazy {
        LocationSearchResultAdapter {
            setSearchedLocation(it)
        }
    }

    private val inputManager by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSearchResult.adapter = locationSearchResultAdapter
        binding.vm = fragmentViewModel
        binding.root.setOnTouchListener { container, event ->
            container.performClick()

            if (event.action == MotionEvent.ACTION_UP) {
                inputManager.hideSoftInputFromWindow(container.windowToken, 0)
                binding.etSearchLocation.clearFocus()
            }
            true
        }

        binding.etSearchLocation.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when (keyCode) {
                    KeyEvent.KEYCODE_ENTER -> {
                        val queryText = binding.etSearchLocation.text.toString()
                        inputManager.hideSoftInputFromWindow(
                            binding.etSearchLocation.windowToken,
                            0
                        )
                        findLocation(queryText)
                        binding.etSearchLocation.clearFocus()
                    }
                    KeyEvent.KEYCODE_BACK -> {
                        binding.etSearchLocation.clearFocus()
                    }
                }
            }
            true
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    fragmentViewModel.locationSearchResult.collectLatest {
                        locationSearchResultAdapter.submitList(it)
                    }
                }
                launch {
                    fragmentViewModel.errorEvent.collectLatest {
                        showSnackBar(it.message ?: DEFAULT_STRING)
                    }
                }
            }
        }
    }

    private fun setSearchedLocation(location: LocationSearchResult) {
        fragmentViewModel.setSearchedResult(listOf())
        activityViewModel.run {
            setChoosedLocation(location.location)
            setLocationName(binding.etSearchLocation.text.toString())
        }
        findNavController().popBackStack()
    }

    private fun findLocation(query: String) {
        fragmentViewModel.searchLocation(query)
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        private const val DEFAULT_STRING = ""
    }
}