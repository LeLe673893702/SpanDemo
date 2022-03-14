package com.example.spandemo.coor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import com.example.spandemo.R

class ConstraintLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_layout)
        findViewById<Button>(R.id.bt_text).setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                v.x = event.rawX - v.width / 2
                v.y = event.rawY - v.height/ 2
            }
            return@setOnTouchListener true
        }
    }
}