package com.woory.almostthere.presentation.model

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.woory.almostthere.presentation.R

enum class ProfileImage(@DrawableRes private val imageResId: Int) {
    MODEST(R.drawable.bg_modest_shiba_in_profile),
    LYING(R.drawable.bg_lying_shiba_in_profile),
    SLEEPY(R.drawable.bg_sleepy_shiba_in_profile),
    STUBBORN(R.drawable.bg_stubborn_shiba_in_profile),
    SUDDEN(R.drawable.bg_sudden_shiba_in_profile);

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawableImage(context: Context): Drawable =
        context.resources.getDrawable(imageResId, null)

    companion object {
        private val imageArray = values().indices

        fun getRandomImage(): Int = imageArray.random()
    }
}