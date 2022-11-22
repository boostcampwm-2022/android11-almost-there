package com.woory.almostthere.binding

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.woory.almostthere.R
import com.woory.almostthere.ui.join.FormState

@BindingAdapter("state_message")
fun TextInputLayout.bindMessage(state: FormState) {
    error = if (state is FormState.Invalid) {
        context.getString(R.string.invalid_invite_code)
    } else {
        ""
    }

    helperText = if (state is FormState.Valid) {
        context.getString(R.string.valid_invite_code)
    } else {
        ""
    }
}

@BindingAdapter("enabled")
fun Button.bindEnabled(state: FormState) {
    isEnabled = state is FormState.Valid
}

@BindingAdapter("visibility")
fun ProgressBar.bindVisibility(isGone: Boolean) {
    visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}