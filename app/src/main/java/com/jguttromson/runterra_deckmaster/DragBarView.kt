package com.jguttromson.runterra_deckmaster

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class ScheduleDragBarView : View {

    private val mainPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).also {
            it.color = resources.getColor(R.color.lightGray)
            it.style = Paint.Style.FILL
        }
    }

    private val screenRectF: RectF = RectF()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas ?: return

        val radius = height.toFloat() / 2

        screenRectF.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(screenRectF, radius, radius, mainPaint)
    }
}