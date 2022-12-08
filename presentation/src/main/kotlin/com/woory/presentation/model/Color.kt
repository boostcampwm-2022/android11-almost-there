package com.woory.presentation.model

import com.woory.presentation.util.getHex
import java.util.Random

data class Color(
    val red: Int,
    val green: Int,
    val blue: Int
) {

    override fun toString(): String = "#${getHex(red)}${getHex(green)}${getHex(blue)}"

    companion object {

        private val random = Random()
        private const val MAX_COLOR_VALUE = 255

        fun getRandomColor(): Color =
            Color(
                random.nextInt(MAX_COLOR_VALUE),
                random.nextInt(MAX_COLOR_VALUE),
                random.nextInt(MAX_COLOR_VALUE)
            )
    }
}