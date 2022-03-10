package com.example.spandemo.text

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.spandemo.R

class AutoLineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_line)
        findViewById<AutoLineTextView>(R.id.tv_text)?.run {
            post {
                setLines(height / lineHeight)
            }
        }
    }
}