package com.woory.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentFindPromiseBinding
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.ui.promises.PromisesActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPromiseFragment :
    BaseFragment<FragmentFindPromiseBinding>(R.layout.fragment_find_promise) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.containerEndedPromise.setOnClickListener {
            val intent = Intent(requireActivity(), PromisesActivity::class.java)
            startActivity(intent)
        }

        binding.containerSoonPromise.setOnClickListener {
            val intent = Intent(requireActivity(), PromisesActivity::class.java)
            startActivity(intent)
        }
    }
}