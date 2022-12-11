package com.woory.presentation.ui.creatingpromise

import android.os.Bundle
import android.view.View
import com.woory.presentation.R
import com.woory.presentation.databinding.DialogFragmentCheckPromiseDataBinding
import com.woory.presentation.model.PromiseData
import com.woory.presentation.ui.BaseDialogFragment
import com.woory.presentation.util.TimeUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckPromiseDataDialog :
    BaseDialogFragment<DialogFragmentCheckPromiseDataBinding>(R.layout.dialog_fragment_check_promise_data) {

    interface ButtonClickListener {
        fun onSubmit()

        fun onCancel()
    }

    private var buttonClickListener: ButtonClickListener? = null

    fun setButtonClickListener(_buttonClickListener: ButtonClickListener) {
        buttonClickListener = _buttonClickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvPromiseLocation.text = requireArguments().getString(PROMISE_LOCATION_KEY)
        binding.tvPromiseDateTime.text = requireArguments().getString(PROMISE_DATE_TIME_KEY)
        binding.tvGameTime.text = requireArguments().getString(GAME_TIME_KEY)

        binding.btnSubmit.setOnClickListener {
            buttonClickListener?.onSubmit()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            buttonClickListener?.onCancel()
            dismiss()
        }
    }

    fun setPromiseData(promiseData: PromiseData) {
        val bundle = Bundle().apply {
            putString(PROMISE_DATE_TIME_KEY, TimeUtils.getOffsetDateTimeToFormatString(promiseData.promiseDateTime))
            putString(PROMISE_LOCATION_KEY, promiseData.promiseLocation.address)
            putString(GAME_TIME_KEY, TimeUtils.getOffsetDateTimeToFormatString(promiseData.gameDateTime))
        }
        arguments = bundle
    }

    companion object {
        private const val PROMISE_DATE_TIME_KEY = "PROMISE_DATE_KEY"
        private const val PROMISE_LOCATION_KEY = "PROMISE_LOCATION_KEY"
        private const val GAME_TIME_KEY = "GAME_TIME_KEY"
    }
}