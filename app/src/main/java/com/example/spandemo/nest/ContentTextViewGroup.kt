package com.example.spandemo.nest

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.spandemo.R

class ContentTextViewGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    init {
       LayoutInflater.from(context).inflate(R.layout.content_text_view_group, this)
    }
}