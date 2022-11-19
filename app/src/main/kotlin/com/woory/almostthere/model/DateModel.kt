package com.woory.almostthere.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DateModel(val year: Int, val month: Int, val dayOfMonth: Int) : Parcelable {

    override fun toString(): String {
        return "${year}년 ${month}월 ${dayOfMonth}일"
    }
}