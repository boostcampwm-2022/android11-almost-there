package com.woory.presentation.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.woory.presentation.R
import com.woory.presentation.ui.join.FormState

@BindingAdapter("stateMessage")
fun TextInputLayout.bindStateMessage(state: FormState) {
    error = if (state is FormState.Invalid) {
        context.getString(R.string.invalid_invite_code)
    } else {
        ""
    }
}