package com.woory.presentation.ui.gameresult

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentCalculateBinding
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalculateFragment : BaseFragment<FragmentCalculateBinding>(R.layout.fragment_calculate),
    AmountDueDialogFragment.ButtonClickListener {

    private val viewModel: GameResultViewModel by activityViewModels()

    private val amountDueDialog by lazy {
        AmountDueDialogFragment().apply {
            setButtonClickListener(this@CalculateFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpButtonClickListener()
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

    private fun setUpButtonClickListener() {
        binding.btnInputAmountDue.setOnClickListener {
            showAmountDueDialog()
        }
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
                        binding.tvPayment.text = if (it != null) {
                            String.format(getString(R.string.payment), it)
                        } else ""
                    }
                }
            }
        }
    }

    private fun showAmountDueDialog() {
        amountDueDialog.show(parentFragmentManager, amountDueDialog.TAG)
    }

    override fun onSubmit(value: Int) {
        viewModel.loadUserPaymentList(value)
    }

    override fun onCancel() {
        if (viewModel.myPayment.value == null) {
            requireActivity().finish()
        }
    }
}