package com.woory.presentation.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.woory.presentation.model.ProfileImage

@BindingAdapter("src")
fun ImageView.bindImage(index: Int) {
    val imageDrawable = ProfileImage.values()[index].getDrawableImage(context)

    setImageDrawable(imageDrawable)
}