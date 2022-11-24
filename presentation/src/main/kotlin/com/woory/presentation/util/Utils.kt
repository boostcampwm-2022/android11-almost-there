package com.woory.presentation.util

import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.navigation.NavOptions
import com.woory.presentation.R

val animRightToLeftNavOption = NavOptions.Builder().apply {
    setEnterAnim(R.anim.slide_in_from_right)
    setExitAnim(R.anim.slide_out_to_left)
    setPopEnterAnim(R.anim.slide_in_from_left)
    setPopExitAnim(R.anim.slide_out_to_right)
}.build()

val animLeftToRightNavOptions = NavOptions.Builder().apply {
    setEnterAnim(R.anim.slide_in_from_left)
    setExitAnim(R.anim.slide_out_to_right)
    setPopEnterAnim(R.anim.slide_in_from_right)
    setPopExitAnim(R.anim.slide_out_to_left)
}.build()

val textScaleAnimation = ScaleAnimation(
    1f, 1.2f, 1f, 1.2f,
    Animation.RELATIVE_TO_SELF, 0.5f,
    Animation.RELATIVE_TO_SELF, 0.5f
).apply {
    repeatCount = Animation.INFINITE
    duration = 1000
    repeatMode = Animation.REVERSE
}

fun getHex(value: Int): String = "%02X".format(value)