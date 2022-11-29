package com.woory.presentation.ui.gameresult

import android.app.Dialog
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

    private val amountDueDialog by lazy {
        Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_amount_due)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        showAmountDueDialog()
    }

    private fun setUpToolbar() {
        (requireActivity() as GameResultActivity).supportActionBar?.show()
    }

    private fun showAmountDueDialog() {
        amountDueDialog.show()
    }
}