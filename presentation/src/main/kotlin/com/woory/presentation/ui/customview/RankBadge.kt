package com.woory.presentation.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.woory.presentation.R
import com.woory.presentation.util.getDip

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
        val padding = getDip(4f)
        setPadding(padding, padding, padding, padding)
        setTextAppearance(R.style.Caption)
        setTextColor(Color.BLACK)
        background = null
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            getDip(100f).toFloat(),
            getDip(100f).toFloat(),
            paint.apply { color = ContextCompat.getColor(context, badgeColor) }
        )

        super.onDraw(canvas)
    }
}