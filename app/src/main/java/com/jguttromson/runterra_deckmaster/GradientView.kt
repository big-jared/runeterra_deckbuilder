package com.jguttromson.runterra_deckmaster

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class GradientView: View {

    val gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader = LinearGradient(0f, 0f, 0f, height.toFloat(), Color.TRANSPARENT, Color.TRANSPARENT, Shader.TileMode.MIRROR)
        style = Paint.Style.FILL_AND_STROKE
    }

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    fun init(card: Card) {
        val color = card.getRegionColor(context)
        gradientPaint.apply {
            shader = LinearGradient(0f, 0f, width.toFloat(), 0f, color, Color.TRANSPARENT, Shader.TileMode.MIRROR)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas ?: return

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), gradientPaint)
    }
}