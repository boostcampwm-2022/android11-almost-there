package com.woory.presentation.model

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.woory.presentation.R

enum class ProfileImage(@DrawableRes private val imageResId: Int) {
    MODEST(R.drawable.bg_modest_shiba),
    LYING(R.drawable.bg_lying_shiba),
    SLEEPY(R.drawable.bg_sleepy_shiba),
    STUBBORN(R.drawable.bg_stubborn_shiba),
    SUDDEN(R.drawable.bg_sudden_shiba);

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawableImage(context: Context): Drawable =
        context.resources.getDrawable(imageResId, null)

    companion object {
        private val imageArray = values().indices

        fun getRandomImage(): Int = imageArray.random()
    }
}