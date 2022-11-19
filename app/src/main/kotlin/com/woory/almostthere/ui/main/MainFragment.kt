package com.woory.almostthere.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.woory.almostthere.R
import com.woory.almostthere.databinding.FragmentMainBinding
import com.woory.almostthere.ui.BaseFragment
import com.woory.almostthere.ui.creatingpromise.CreatingPromiseActivity
import com.woory.almostthere.util.animLeftToRightNavOptions
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
                R.id.nav_join_activity,
                null,
                animLeftToRightNavOptions
            )
        }

        binding.containerCreatePromise.setOnClickListener {
            val intent = Intent(requireActivity(), CreatingPromiseActivity()::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
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