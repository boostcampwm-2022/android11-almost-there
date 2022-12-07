package com.woory.presentation.binding

import android.widget.Button
import androidx.databinding.BindingAdapter
import com.woory.presentation.ui.join.FormState

@BindingAdapter("enabled")
fun Button.bindEnabled(state: FormState) {
    isEnabled = state is FormState.Valid
}