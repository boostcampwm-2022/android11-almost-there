package com.woory.almostthere.util

import androidx.navigation.NavOptions
import com.woory.almostthere.R

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