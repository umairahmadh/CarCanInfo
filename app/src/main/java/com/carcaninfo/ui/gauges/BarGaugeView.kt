package com.carcaninfo.ui.gauges

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Horizontal bar gauge view
 */
class BarGaugeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    // Paint objects
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1E1E1E")
        style = Paint.Style.FILL
    }
    
    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.LEFT
    }
    
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#B3B3B3")
        textSize = 28f
        textAlign = Paint.Align.LEFT
    }
    
    // Gauge properties
    var value: Float = 0f
        set(value) {
            field = value.coerceIn(minValue, maxValue)
            updateBarColor()
            invalidate()
        }
    
    var minValue: Float = 0f
    var maxValue: Float = 100f
    var label: String = ""
    var unit: String = ""
    var warningThreshold: Float = 80f
    var criticalThreshold: Float = 90f
    
    init {
        updateBarColor()
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val padding = 20f
        val barHeight = 60f
        val labelHeight = 80f
        
        // Draw label and value
        canvas.drawText(label, padding, padding + 30f, labelPaint)
        canvas.drawText(
            String.format("%.0f %s", value, unit),
            padding,
            padding + labelHeight,
            textPaint
        )
        
        // Draw background bar
        val barTop = padding + labelHeight + 10f
        val backgroundRect = RectF(
            padding,
            barTop,
            width - padding,
            barTop + barHeight
        )
        canvas.drawRoundRect(backgroundRect, 10f, 10f, backgroundPaint)
        
        // Draw value bar
        val percentage = (value - minValue) / (maxValue - minValue)
        val barWidth = (width - 2 * padding) * percentage
        val valueRect = RectF(
            padding,
            barTop,
            padding + barWidth,
            barTop + barHeight
        )
        canvas.drawRoundRect(valueRect, 10f, 10f, barPaint)
    }
    
    private fun updateBarColor() {
        val percentage = ((value - minValue) / (maxValue - minValue)) * 100f
        barPaint.color = when {
            percentage >= criticalThreshold -> Color.parseColor("#F44336")  // Red
            percentage >= warningThreshold -> Color.parseColor("#FF9800")   // Orange
            else -> Color.parseColor("#4CAF50")                              // Green
        }
    }
}
