package com.woory.almostthere.presentation.util

import android.view.View
import android.widget.ProgressBar
import com.google.android.material.snackbar.Snackbar

fun showSnackBar(view: View, message: String) =
    Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_SHORT
    ).show()

fun handleLoading(progressBar: ProgressBar, isLoading: Boolean) {
    progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
}