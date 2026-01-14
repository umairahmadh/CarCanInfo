package com.carcaninfo.ui.gauges

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Analog gauge view with needle indicator
 */
class AnalogGaugeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    // Paint objects
    private val dialPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#2196F3")
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }
    
    private val needlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F44336")
        strokeWidth = 6f
        strokeCap = Paint.Cap.ROUND
    }
    
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
    
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#B3B3B3")
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }
    
    // Gauge properties
    var value: Float = 0f
        set(value) {
            field = value.coerceIn(minValue, maxValue)
            invalidate()
        }
    
    var minValue: Float = 0f
    var maxValue: Float = 100f
    var label: String = ""
    var unit: String = ""
    
    // Angles for gauge (220 degrees total, starting at 160 degrees)
    private val startAngle = 160f
    private val sweepAngle = 220f
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(width, height) / 2f - 40f
        
        // Draw gauge arc
        val rect = RectF(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )
        canvas.drawArc(rect, startAngle, sweepAngle, false, dialPaint)
        
        // Draw tick marks
        drawTickMarks(canvas, centerX, centerY, radius)
        
        // Draw needle
        drawNeedle(canvas, centerX, centerY, radius)
        
        // Draw value text
        canvas.drawText(
            String.format("%.0f", value),
            centerX,
            centerY + 20f,
            textPaint
        )
        
        // Draw unit text
        canvas.drawText(
            unit,
            centerX,
            centerY + 60f,
            labelPaint
        )
        
        // Draw label
        canvas.drawText(
            label,
            centerX,
            height - 40f,
            labelPaint
        )
    }
    
    private fun drawTickMarks(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#B3B3B3")
            strokeWidth = 2f
        }
        
        val numTicks = 11
        for (i in 0 until numTicks) {
            val angle = Math.toRadians((startAngle + (sweepAngle * i / (numTicks - 1))).toDouble())
            val startX = centerX + (radius - 20f) * cos(angle).toFloat()
            val startY = centerY + (radius - 20f) * sin(angle).toFloat()
            val endX = centerX + radius * cos(angle).toFloat()
            val endY = centerY + radius * sin(angle).toFloat()
            
            canvas.drawLine(startX, startY, endX, endY, tickPaint)
        }
    }
    
    private fun drawNeedle(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        val percentage = (value - minValue) / (maxValue - minValue)
        val angle = Math.toRadians((startAngle + sweepAngle * percentage).toDouble())
        
        val needleLength = radius - 30f
        val endX = centerX + needleLength * cos(angle).toFloat()
        val endY = centerY + needleLength * sin(angle).toFloat()
        
        canvas.drawLine(centerX, centerY, endX, endY, needlePaint)
        
        // Draw center circle
        canvas.drawCircle(centerX, centerY, 12f, needlePaint)
    }
}
