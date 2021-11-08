package com.example.spandemo

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.style.BackgroundColorSpan
import android.text.style.ReplacementSpan
import android.util.Log
import android.util.TypedValue
import androidx.annotation.ColorRes

class OfficialTextSpan(private val radius: Float, @ColorRes private val color: Int):ReplacementSpan(){
    private val xPadding = DensityUtils.dpToPx(8).toFloat()
    private val yPadding = DensityUtils.dpToPx(5).toFloat()
    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        if (text.isNullOrEmpty()) return 0
        return (paint.measureText(text, start, end) + xPadding *2).toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        if (text.isNullOrEmpty()) return
        paint.color = Color.BLUE
        paint.textSize = spToPx(6f)
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        val fm = paint.fontMetrics

        val width = (paint.measureText(text, start, end) + xPadding *2).toInt()
        val height = fm.bottom - fm.top+ yPadding*2
        Log.d("OfficialTextSpan", "$top...$bottom...${fm.ascent}...${fm.descent}...${fm.bottom}...${fm.top}...$y")
        canvas.save()
        canvas.translate(x, 0f)
        canvas.drawRoundRect(x,0f, x+width,
            height, radius, radius, paint)
//0...66...-36.54...9.87...9.87...-36.54...52
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER

        val centerY = height / 2f - (fm.bottom + fm.top) / 2f
        canvas.drawText(text, start, end, x+width/2, centerY, paint)
        canvas.restore()
    }

    private fun spToPx(spSize:Float):Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spSize,
            Resources.getSystem().displayMetrics
        )
    }
}