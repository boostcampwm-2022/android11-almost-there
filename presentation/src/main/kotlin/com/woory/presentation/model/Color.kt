package com.woory.presentation.model

import com.woory.presentation.util.getHex

data class Color(
    val red: Int,
    val green: Int,
    val blue: Int
) {

    override fun toString(): String = "#${getHex(red)}${getHex(green)}${getHex(blue)}"

    companion object {
        private val rgbRange = (0..255)

        fun getRandomColor(): Color =
            Color(
                rgbRange.random(),
                rgbRange.random(),
                rgbRange.random()
            )
    }
}