package com.woory.almostthere.presentation.util

import android.content.Context
import dagger.hilt.android.internal.managers.ViewComponentManager

fun getActivityContext(context: Context): Context {
    return if (context is ViewComponentManager.FragmentContextWrapper) {
        context.baseContext
    } else context
}