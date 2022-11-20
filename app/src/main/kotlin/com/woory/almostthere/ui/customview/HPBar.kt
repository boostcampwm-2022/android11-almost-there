package com.woory.almostthere.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.woory.almostthere.R

class HPBar : View {

    private var _value: Int = 100
    private var _minValue: Int = 0
    private var _maxValue: Int = 100

    private var _textVisible: Boolean = false
    private var _backgroundVisible: Boolean = false
    private val _color: Int
        get() = when (_value / (_maxValue - _minValue) * 100) {
            in 80 .. 100 -> Color.GREEN
            in 0 .. 20 -> Color.RED
            else -> Color.YELLOW
        }

    private var _textPadding: Float = 10F
    private var _backgroundColor: Int = Color.LTGRAY

    private val paint: Paint = Paint()
    private val textPaint: TextPaint = TextPaint()

    private val rectF: RectF = RectF()

    var value: Int
        get() = _value
        set(value) {
            _value = value
            invalidate()
        }

    var minValue: Int
        get() = _minValue
        set(value) {
            _minValue = value
            invalidate()
        }

    var maxValue: Int
        get() = _maxValue
        set(value) {
            _maxValue = value
            invalidate()
        }

    var textVisible: Boolean
        get() = _textVisible
        set(value) {
            _textVisible = value
            invalidate()
        }

    var backgroundVisible: Boolean
        get() = _backgroundVisible
        set(value) {
            _backgroundVisible = value
            invalidate()
        }

    var textPadding: Float
        get() = _textPadding
        set(value) {
            _textPadding = value
            invalidate()
        }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.HPBar, defStyle, 0
        )

        _value = a.getInt(R.styleable.HPBar_value, _value)
        _minValue = a.getInt(R.styleable.HPBar_minValue, _minValue)
        _maxValue = a.getInt(R.styleable.HPBar_maxValue, _maxValue)
        _textVisible = a.getBoolean(R.styleable.HPBar_textVisible, _textVisible)
        _backgroundVisible = a.getBoolean(R.styleable.HPBar_backgroundVisible, _backgroundVisible)
        _backgroundColor = a.getColor(R.styleable.HPBar_backgroundColor, _backgroundColor)

        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = (width - width * (_maxValue - _minValue - _value) / (_maxValue - _minValue) - paddingRight).toFloat()
        val contentHeight = (height - paddingBottom).toFloat()

        if (_backgroundVisible) {
            paint.color = _backgroundColor
            rectF.set(0F, 0F, width.toFloat(), height.toFloat())
            canvas.drawRoundRect(rectF, 20F, 20F, paint)
        }

        paint.color = _color
        rectF.set(paddingLeft.toFloat(), paddingTop.toFloat(), contentWidth, contentHeight)
        canvas.drawRoundRect(rectF, 20F, 20F, paint)

        if (_textVisible) {
            textPaint.color = Color.BLACK
            textPaint.textSize = contentHeight - _textPadding
            canvas.drawText(_value.toString(), rectF.left + textPadding, rectF.bottom - textPadding, textPaint)
        }
    }
}