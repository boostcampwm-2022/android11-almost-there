package com.woory.presentation.ui.creatingpromise

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentProfileBinding
import com.woory.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {

    private val viewModel: CreatingPromiseViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSetProfile.setOnClickListener {
            viewModel.setUser()
            findNavController().navigate(R.id.nav_creating_promise_frag)
        }
    }
}