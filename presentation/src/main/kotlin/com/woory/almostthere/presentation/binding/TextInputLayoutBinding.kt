package com.woory.almostthere.presentation.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.ui.join.FormState

@BindingAdapter("stateMessage")
fun TextInputLayout.bindStateMessage(state: FormState) {
    error = if (state is FormState.Invalid) {
        context.getString(R.string.invalid_invite_code)
    } else {
        ""
    }
}