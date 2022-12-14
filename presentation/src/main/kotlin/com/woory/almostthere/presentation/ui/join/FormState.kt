package com.woory.almostthere.presentation.ui.join

sealed class FormState {

    data class Valid(
        val message: String = ""
    ) : FormState()

    data class Invalid(
        val message: String = ""
    ) : FormState()

    object EMPTY : FormState()
}