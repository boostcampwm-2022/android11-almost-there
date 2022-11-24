package com.woory.presentation.ui.promiseinfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.skt.tmap.TMapView
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentPromiseInfoBinding
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.util.MAP_API_KEY
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PromiseInfoFragment :
    BaseFragment<FragmentPromiseInfoBinding>(R.layout.fragment_promise_info) {

    private val viewModel: PromiseInfoViewModel by activityViewModels()
    private lateinit var tMapView: TMapView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tMapView = binding.mapPromiseLocation

        viewModel.fetchPromiseDate()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel
        binding.defaultString = ""

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorState.collect {
                if (it) {
                    Snackbar.make(binding.root, "약속 가져오기에 실패하였습니다.", Snackbar.LENGTH_SHORT).show()
                    viewModel.setUiState(PromiseUiState.Loading)
                    viewModel.setErrorState(false)
                }
            }
        }
    }
}