package com.woory.presentation.ui.gameresult

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentGameResultBinding
import com.woory.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameResultFragment : BaseFragment<FragmentGameResultBinding>(R.layout.fragment_game_result) {

    private val viewModel: GameResultViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpClickListener()
    }

    private fun setUpToolbar() {
        (requireActivity() as GameResultActivity).supportActionBar?.hide()
    }

    private fun setUpClickListener() {
        binding.btnCalculate.setOnClickListener {
            findNavController().navigate(R.id.nav_calculate_frag)
        }

        binding.btnExit.setOnClickListener {
            requireActivity().finish()
        }
    }
}
