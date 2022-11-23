package com.woory.firebase.util

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

    fun String.isValidInviteCode(): Boolean = matches(inviteCodeRegex)
}