package com.woory.almostthere.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.woory.almostthere.R
import com.woory.almostthere.databinding.FragmentFindPromiseBinding
import com.woory.almostthere.util.animLeftToRightNavOptions
import com.woory.almostthere.util.animRightToLeftNavOption

class FindPromiseFragment : Fragment() {

    private var _binding: FragmentFindPromiseBinding? = null
    private val binding: FragmentFindPromiseBinding get() = requireNotNull(_binding)

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_find_promise, container, false)
        return binding.root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}