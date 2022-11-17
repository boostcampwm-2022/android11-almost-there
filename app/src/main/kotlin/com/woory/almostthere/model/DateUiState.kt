package com.woory.almostthere.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DateUiState(val year: Int, val month: Int, val dayOfMonth: Int) : Parcelable {
    override fun toString(): String {
        return "$year-$month-$dayOfMonth"
    }
}