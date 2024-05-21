package com.base.app.ui.custom_view_study

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View

/**
 * @author tuanpham
 * @since 5/19/2024
 */
class CustomViewStudy(context: Context) : View(context) {
    private var path: Path = Path()
    private var paint: Paint = Paint()

    init {
        paintSettings()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(path, paint)
    }

    private fun paintSettings() {
        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.strokeWidth = 10f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val pointX = event?.x ?: 0f
        val pointY = event?.y ?: 0f
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(pointX, pointY)
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(pointX, pointY)
            }

            MotionEvent.ACTION_UP -> {
                path.reset()
            }
        }
        invalidate()
        return true
    }
}