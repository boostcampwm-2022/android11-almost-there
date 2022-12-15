package com.woory.almostthere.presentation.model

data class PromiseHistory(
    val userId: String? = null,
    val promise: Promise,
    val magnetic: MagneticInfo? = null,
    val users: List<UserHp>? = null
) {

    val ranking: List<String>?
        get() =
            users?.sortedByDescending { user -> user.hp }
                ?.map { it.userId }
                ?.map { userId ->
                    promise.data.users.filter { userInfo ->
                        userId == userInfo.userId
                    }
                        .map { it.data.name }
                }?.flatten()

    val userHP: Int
        get() = users?.firstOrNull() { it.userId == userId }?.hp ?: 100
}