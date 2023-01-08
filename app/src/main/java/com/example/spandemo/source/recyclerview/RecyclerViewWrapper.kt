package com.example.spandemo.source.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewWrapper @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {
    private var layoutListener: LayoutListener? = null

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        Log.e("SourceRecyclerViewActivity", "before onMeasure")

        super.onMeasure(widthSpec, heightSpec)

        Log.e("SourceRecyclerViewActivity", "after onMeasure")
    }

    fun setLayoutListener(layoutListener: LayoutListener?) {
        this.layoutListener = layoutListener
    }

    override fun addView(child: View?) {
        super.addView(child)
        Log.e("SourceRecyclerViewActivity", "addView")

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (layoutListener != null) {
            layoutListener!!.onBeforeLayout()
        }
        super.onLayout(changed, l, t, r, b)
        if (layoutListener != null) {
            layoutListener!!.onAfterLayout()
        }
    }

    interface LayoutListener {
        fun onBeforeLayout()
        fun onAfterLayout()
    }

    override fun requestLayout() {
        Log.e("SourceRecyclerViewActivity", "requestLayout")
        super.requestLayout()
    }
}