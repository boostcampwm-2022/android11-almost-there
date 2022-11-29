package com.woory.presentation.ui.gameresult

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentCalculateBinding
import com.woory.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalculateFragment : BaseFragment<FragmentCalculateBinding>(R.layout.fragment_calculate) {

    private val viewModel: GameResultViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
    }

    private fun setUpToolbar() {
        (requireActivity() as GameResultActivity).supportActionBar?.show()
    }
}