package com.woory.almostthere.presentation.util

import com.woory.almostthere.presentation.ui.join.FormState

object InviteCodeUtil {

    private const val MAX_INVITE_CODE_LENGTH = 7
    private val inviteCodeRegex =
        """[A-Z0-9]{$MAX_INVITE_CODE_LENGTH}""".toRegex(RegexOption.IGNORE_CASE)

    fun getCodeState(code: String): FormState = if (code.isEmpty()) {
        FormState.EMPTY
    } else if (code.matches(inviteCodeRegex)) {
        FormState.Valid()
    } else {
        FormState.Invalid()
    }
}