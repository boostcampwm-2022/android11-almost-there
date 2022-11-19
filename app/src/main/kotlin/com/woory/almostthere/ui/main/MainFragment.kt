package com.woory.almostthere.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.woory.almostthere.R
import com.woory.almostthere.databinding.FragmentMainBinding
import com.woory.almostthere.ui.viewBinding
import com.woory.almostthere.util.animLeftToRightNavOptions
import com.woory.almostthere.util.animRightToLeftNavOption
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.containerJoinPromise.setOnClickListener {
            navController.navigate(
                R.id.nav_join_activity,
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