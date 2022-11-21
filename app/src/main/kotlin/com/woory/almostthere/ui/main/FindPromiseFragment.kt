package com.woory.almostthere.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.woory.almostthere.R
import com.woory.almostthere.databinding.FragmentFindPromiseBinding
import com.woory.almostthere.ui.BaseFragment
import com.woory.almostthere.ui.promises.PromisesActivity
import com.woory.almostthere.util.animLeftToRightNavOptions
import com.woory.almostthere.util.animRightToLeftNavOption
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