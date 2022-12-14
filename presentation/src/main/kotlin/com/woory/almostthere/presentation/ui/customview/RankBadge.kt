package com.woory.almostthere.presentation.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.woory.almostthere.presentation.R
import com.woory.almostthere.presentation.util.getDip

class RankBadge constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    @ColorRes
    var badgeColor = R.color.gold
        set(value) {
            field = value
            invalidate()
        }

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        val padding = getDip(6f)
        setPadding(padding, padding, padding, padding)
        setTextAppearance(R.style.Caption)
        textSize = 12f
        setTextColor(Color.BLACK)
        background = null
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(
            width.toFloat() / 2,
            height.toFloat() / 2,
            minOf(width, height).toFloat() / 2,
            paint.apply { color = ContextCompat.getColor(context, badgeColor) }
        )

        super.onDraw(canvas)
    }
}