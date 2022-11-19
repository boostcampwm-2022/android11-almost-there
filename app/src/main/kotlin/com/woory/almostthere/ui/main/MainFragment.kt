package com.woory.almostthere.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.woory.almostthere.R
import com.woory.almostthere.databinding.FragmentMainBinding
import com.woory.almostthere.ui.BaseFragment
import com.woory.almostthere.util.animLeftToRightNavOptions
import com.woory.almostthere.util.animRightToLeftNavOption
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.containerJoinPromise.setOnClickListener {
            navController.navigate(
                R.id.nav_dummy_join_frag,
                null,
                animLeftToRightNavOptions
            )
        }

        binding.containerCreatePromise.setOnClickListener {
            navController.navigate(
                R.id.nav_dummy_create_frag,
                null,
                animRightToLeftNavOption
            )
        }

        binding.containerFindPromise.setOnClickListener {
            navController.navigate(
                R.id.nav_find_promise_frag,
                null,
                animLeftToRightNavOptions
            )
        }
    }
}