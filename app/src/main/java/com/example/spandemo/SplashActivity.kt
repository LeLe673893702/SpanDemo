package com.example.spandemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.taptap_theme_translucent)
        setContentView(R.layout.activity_splash)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}