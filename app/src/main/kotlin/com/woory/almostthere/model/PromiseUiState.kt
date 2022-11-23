package com.woory.almostthere.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromiseUiState(
    val code: String = ""
) : Parcelable