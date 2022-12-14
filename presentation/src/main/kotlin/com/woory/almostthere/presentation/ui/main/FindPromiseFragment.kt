package com.woory.almostthere.presentation.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.databinding.FragmentFindPromiseBinding
import com.woory.almostthere.presentation.ui.BaseFragment
import com.woory.almostthere.presentation.ui.history.PromiseHistoryType
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