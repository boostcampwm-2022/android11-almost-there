package com.woory.presentation.util

import android.view.View
import androidx.annotation.Px

@Px
fun View.getDip(value: Float) = (value * resources.displayMetrics.density).toInt()