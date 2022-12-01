package com.woory.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentFindPromiseBinding
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.ui.history.PromiseHistoryType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPromiseFragment :
    BaseFragment<FragmentFindPromiseBinding>(R.layout.fragment_find_promise) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.containerEndedPromise.setOnClickListener {
            val action = MainFragmentDirections.startPromiseHistoryActivity(PromiseHistoryType.PAST)
            findNavController().navigate(action)
        }

        binding.containerSoonPromise.setOnClickListener {
            val action = MainFragmentDirections.startPromiseHistoryActivity(PromiseHistoryType.FUTURE)
            findNavController().navigate(action)
        }
    }
}