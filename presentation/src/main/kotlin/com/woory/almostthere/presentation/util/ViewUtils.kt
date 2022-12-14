package com.woory.almostthere.presentation.util

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

fun showDialog(context: Context, message: String, neutralTitle: String, action: () -> Unit) {
    MaterialAlertDialogBuilder(context)
        .setMessage(message)
        .setNeutralButton(neutralTitle) { _, _ ->
            action()
        }
        .show()
}