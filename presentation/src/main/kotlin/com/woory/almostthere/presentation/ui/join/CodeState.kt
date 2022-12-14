package com.woory.almostthere.presentation.ui.join

import android.content.Context
import androidx.annotation.StringRes
import com.woory.almostthere.presentation.R

enum class CodeState(@StringRes private val messageResId: Int) {
    NONEXISTENT(R.string.invalid_invite_code),
    ALREADY_JOIN(R.string.already_join),
    ALREADY_STARTED(R.string.already_started);

    fun getMessage(context: Context): String =
        context.getString(messageResId)
}