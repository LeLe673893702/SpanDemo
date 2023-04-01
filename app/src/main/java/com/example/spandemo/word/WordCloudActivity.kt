package com.example.spandemo.word

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.spandemo.DensityUtils
import com.example.spandemo.R

class WordCloudActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_cloud)
        val strings = listOf<String>("Free", "Novel construction mechanics", "Too many ads", "Bright graphics",
            "Accessible gameplay", "Positive", "MinecraftMinecraft")
        findViewById<WordCloudLayout>(R.id.word_cloud_layout).run {
            repeat(strings.size) {
                val textView = TextView(context)
                textView.text = strings[it]
                textView.gravity = Gravity.CENTER
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                addView(textView, ViewGroup.MarginLayoutParams(-2, DensityUtils.dpToPx(30)).apply {
                    marginStart = DensityUtils.dpToPx(10)
                    topMargin = DensityUtils.dpToPx(10)
                    if (it == strings.size - 1) marginEnd = DensityUtils.dpToPx(10)
                    textView.setPadding(DensityUtils.dpToPx(8), DensityUtils.dpToPx(4), DensityUtils.dpToPx(8), DensityUtils.dpToPx(4))
                })
            }
        }
    }
}