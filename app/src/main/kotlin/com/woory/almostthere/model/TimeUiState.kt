package com.woory.almostthere.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class TimeUiState(val hour: Int, val minute: Int): Parcelable {
    override fun toString(): String {
        return "$hour:$minute"
    }
}