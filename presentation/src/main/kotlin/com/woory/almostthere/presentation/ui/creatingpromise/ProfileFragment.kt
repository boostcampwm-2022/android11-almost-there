package com.woory.almostthere.presentation.ui.creatingpromise

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.databinding.FragmentProfileBinding
import com.woory.almostthere.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val viewModel: CreatingPromiseViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.nav_creating_promise_frag)
        }
    }
}