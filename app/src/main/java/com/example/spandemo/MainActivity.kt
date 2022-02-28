package com.example.spandemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.taptap_theme_translucent)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        var a = 0
        Log.d("MainActivity", "startLoad")
        val text = findViewById<TextView>(R.id.text)
        val officialString = "官方111"
        val spannableString = SpannableString(officialString+"\n")
        val officialTextSpan = OfficialTextSpan(DensityUtils.dpToPx(4).toFloat(), R.color.cardview_shadow_end_color)
        spannableString.setSpan(officialTextSpan,0,officialString.length-1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE )

        text.text = spannableString
    }

    companion object {
        private var resultBack: ((Boolean) -> Unit?)? = null

        fun start(context: Context, resultBack: ((Boolean) -> Unit?)) {
            this.resultBack = resultBack
        }
    }
}