package com.woory.almostthere.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromiseInfoUiState(
    val promiseLocation: LocationUiState,
    val promiseDate: DateUiState,
    val promiseTimeUiState: TimeUiState,
    val gameTimeUiState: TimeUiState,
    val host: UserState,
    val users: List<UserState>
): Parcelable