package com.woory.almostthere.presentation.ui.gameresult

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.databinding.DialogFragmentTotalCostBinding
import com.woory.almostthere.presentation.ui.BaseDialogFragment
import com.woory.almostthere.presentation.util.extractNumber
import com.woory.almostthere.presentation.util.getCommaNumber
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TotalCostFragment :
    BaseDialogFragment<DialogFragmentTotalCostBinding>(R.layout.dialog_fragment_total_cost) {

    private val maxAmountDue = 5000000

    interface ButtonClickListener {
        fun onSubmit(value: Int)

        fun onCancel()
    }

    private var buttonClickListener: ButtonClickListener? = null

    fun setButtonClickListener(_buttonClickListener: ButtonClickListener) {
        buttonClickListener = _buttonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpButtonClickListener()
        setUpEditText()
    }

    private fun setUpEditText() {

        var filteredString = ""
        binding.etAmountDue.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == filteredString) return
                filteredString =
                    when {
                        s.isNullOrEmpty() -> ""
                        s.toString().startsWith("0") -> ""
                        s.toString().extractNumber() > maxAmountDue -> maxAmountDue.getCommaNumber()
                        else -> {
                            s.toString().extractNumber().getCommaNumber()
                        }
                    }

                binding.etAmountDue.setText(filteredString)
                binding.btnSubmit.isEnabled = when (filteredString) {
                    "" -> false
                    else -> true
                }
                binding.etAmountDue.setSelection(filteredString.length)
            }
        })
    }

    private fun setUpButtonClickListener() {
        binding.btnSubmit.setOnClickListener {
            buttonClickListener?.onSubmit(binding.etAmountDue.text.toString().extractNumber())
            dialog?.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            buttonClickListener?.onCancel()
            dialog?.dismiss()
        }
    }
}