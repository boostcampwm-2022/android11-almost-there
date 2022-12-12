package com.woory.presentation.ui.gameresult

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.woory.presentation.R
import com.woory.presentation.databinding.FragmentSplitMoneyBinding
import com.woory.presentation.ui.BaseFragment
import com.woory.presentation.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplitMoneyFragment : BaseFragment<FragmentSplitMoneyBinding>(R.layout.fragment_split_money),
    TotalCostFragment.ButtonClickListener {

    private val viewModel: GameResultViewModel by activityViewModels()

    private val amountDueDialog by lazy {
        TotalCostFragment().apply {
            setButtonClickListener(this@SplitMoneyFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpButtonClickListener()
        setUpAdapter()
        observeData()
    }

    private fun setUpToolbar() {
        (requireActivity() as GameResultActivity).supportActionBar?.show()
    }

    private fun setUpAdapter() {
        binding.rvPayments.adapter = UserSplitMoneyAdapter()
    }

    private fun setUpButtonClickListener() {
        binding.btnInputAmountDue.btnSubmit.setOnClickListener {
            viewModel.setShowDialogEvent()
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.userSplitMoneyItems.collectLatest {
                        (binding.rvPayments.adapter as UserSplitMoneyAdapter).submitList(it)
                    }
                }

                launch {
                    viewModel.mySplitMoney.collectLatest {
                        binding.tvPayment.text = if (it != null) {
                            String.format(getString(R.string.payment), it)
                        } else getString(R.string.null_value)
                    }
                }

                launch {
                    viewModel.showDialogEvent.collectLatest {
                        showAmountDueDialog()
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
        if (viewModel.mySplitMoney.value == null) {
            requireActivity().finish()
        }
    }
}