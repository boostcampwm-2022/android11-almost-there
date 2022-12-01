package com.woory.presentation.ui.history

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.woory.presentation.R

enum class RankBadgeType(
    @StringRes val labelResId: Int,
    @ColorRes val colorResId: Int
) {
    RANK1(R.string.rank1, R.color.gold),
    RANK2(R.string.rank2, R.color.silver),
    RANK3(R.string.rank3, R.color.bronze)
}