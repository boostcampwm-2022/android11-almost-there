package com.woory.almostthere.presentation.util

import com.woory.almostthere.presentation.ui.join.FormState

object InviteCodeUtil {

    private const val MAX_INVITE_CODE_LENGTH = 7
    private val codePool = (('A'..'Z') + (0..9)).shuffled()
    private val inviteCodeRegex =
        """[A-Z0-9]{$MAX_INVITE_CODE_LENGTH}""".toRegex(RegexOption.IGNORE_CASE)

    fun getRandomInviteCode(): String {
        val builder = StringBuilder()

        repeat(MAX_INVITE_CODE_LENGTH) {
            builder.append(codePool.random())
        }

        return builder.toString()
    }

    fun getCodeState(code: String): FormState = if (code.isEmpty()) {
        FormState.EMPTY
    } else if (code.matches(inviteCodeRegex)) {
        FormState.Valid()
    } else {
        FormState.Invalid()
    }
}