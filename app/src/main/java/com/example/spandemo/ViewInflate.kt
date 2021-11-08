package com.example.spandemo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout

class ViewInflate(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_inflate_layout, null)
        Log.d("ViewInflate", view.layoutParams?.toString() ?: "null")
        if (view.layoutParams == null) {
            val lp = ViewGroup.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            this.layoutParams = lp
        }
        addView(view, null)
        Log.d("ViewInflate", view.layoutParams?.toString() ?: "null")
    }
}