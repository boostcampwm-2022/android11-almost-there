package com.woory.presentation.ui.gameresult

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentCalculateBinding
import com.woory.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        setUpDialogButtonClickListener()
        showAmountDueDialog()
        setUpAdapter()
        observeData()
    }

    private fun setUpToolbar() {
        (requireActivity() as GameResultActivity).supportActionBar?.show()
    }

    private fun setUpAdapter() {
        binding.rvPayments.adapter = UserPaymentAdapter()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.userPaymentList.collectLatest {
                        (binding.rvPayments.adapter as UserPaymentAdapter).submitList(it)
                    }
                }

                launch {
                    viewModel.myPayment.collectLatest {
                        binding.tvPayment.text = String.format(getString(R.string.payment), it)
                    }
                }
            }
        }
    }

    private fun setUpDialogButtonClickListener() {
        amountDueDialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            amountDueDialog.cancel()
        }

        amountDueDialog.findViewById<Button>(R.id.btn_submit).setOnClickListener {
            amountDueDialog.cancel()
        }
    }

    private fun showAmountDueDialog() {
        amountDueDialog.show()
    }
}