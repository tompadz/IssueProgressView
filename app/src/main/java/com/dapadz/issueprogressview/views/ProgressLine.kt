package com.dapadz.issueprogressview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class ProgressLine(context: Context, attrs:AttributeSet? = null) : View(context, attrs) {

    private val rect = RectF()

    private val linePaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.GRAY
        isAntiAlias = true
    }

    val minWidth = 25f
    private val lineHeight = 25f

    var color : Int = -1
        set(value) {
            field = value
            linePaint.color = ContextCompat.getColor(context, color)
            invalidate()
        }

    var lineWidth : Float = minWidth
        set(value) {
            field = value.coerceAtLeast(minWidth)
            updateSize()
            invalidate()
        }

    var progressId = 0

    private fun updateSize() {
        rect.set(0f, 0f, lineWidth, lineHeight)
    }

    override fun onMeasure(widthMeasureSpec : Int, heightMeasureSpec : Int) {
        val width = MeasureSpec.makeMeasureSpec(lineWidth.toInt(), MeasureSpec.UNSPECIFIED)
        val height  = MeasureSpec.makeMeasureSpec(lineHeight.toInt(), MeasureSpec.UNSPECIFIED)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas : Canvas?) {
        with(canvas !!) {
            drawRoundRect(rect, lineHeight, lineHeight, linePaint)
        }
    }

    override fun onSizeChanged(w : Int, h : Int, oldw : Int, oldh : Int) = updateSize()
}