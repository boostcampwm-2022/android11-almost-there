package com.woory.almostthere.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.woory.almostthere.R
import com.woory.almostthere.databinding.FragmentFindPromiseBinding
import com.woory.almostthere.ui.viewBinding
import com.woory.almostthere.util.animLeftToRightNavOptions
import com.woory.almostthere.util.animRightToLeftNavOption
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPromiseFragment : Fragment() {

    private val binding: FragmentFindPromiseBinding by viewBinding(FragmentFindPromiseBinding::bind)

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.containerEndedPromise.setOnClickListener {
            navController.navigate(
                R.id.nav_dummy_ended_frag,
                null,
                animLeftToRightNavOptions
            )
        }

        binding.containerSoonPromise.setOnClickListener {
            navController.navigate(
                R.id.nav_dummy_almost_frag,
                null,
                animRightToLeftNavOption
            )
        }
    }
}