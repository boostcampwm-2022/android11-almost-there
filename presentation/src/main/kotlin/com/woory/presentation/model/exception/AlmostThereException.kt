package com.woory.presentation.model.exception

import androidx.annotation.StringRes
import com.woory.presentation.R

sealed class AlmostThereException(@StringRes open val messageResId: Int) {

    data class InvalidCodeException(@StringRes override val messageResId: Int = R.string.invalid_invite_code) :
        AlmostThereException(messageResId)

    data class AlreadyJoinedPromiseException(@StringRes override val messageResId: Int = R.string.already_join) :
        AlmostThereException(messageResId)

    data class AlreadyStartedPromiseException(@StringRes override val messageResId: Int = R.string.already_started) :
        AlmostThereException(messageResId)

    data class FetchFailedException(@StringRes override val messageResId: Int = R.string.fetch_data_fail_message) :
        AlmostThereException(messageResId)

    data class StoreFailedException(@StringRes override val messageResId: Int = R.string.store_data_fail_message) :
        AlmostThereException(messageResId)
}