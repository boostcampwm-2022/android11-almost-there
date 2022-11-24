package com.woory.presentation.binding

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputLayout
import com.woory.presentation.R
import com.woory.presentation.model.Color
import com.woory.presentation.ui.join.FormState
import com.woory.presentation.ui.join.ProfileImage

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
fun ProgressBar.bindVisibility(isLoading: Boolean) {
    visibility = if (isLoading) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("backgroundColor")
fun MaterialCardView.bindBackgroundColor(color: Color) {
    setCardBackgroundColor(android.graphics.Color.parseColor(color.toString()))
}

@BindingAdapter("src")
fun ImageView.bindImage(index: Int) {
    val imageDrawable = ProfileImage.values()[index].getDrawableImage(context)

    setImageDrawable(imageDrawable)
}