package com.example.spandemo.nest

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.spandemo.R

class NestWithRecyclerViewActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest_with_recycler_view)
        val llContent = findViewById<LinearLayout>(R.id.ll_content)
        llContent.apply {
            for (i in 0 until this.childCount) {
                getChildAt(i).visibility = View.GONE
            }
            addView(TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                setBackgroundColor(Color.RED)
                gravity = Gravity.CENTER
                post {
                    ExpandTouchDelegateHelper((llContent.parent as? ViewGroup)!!, this, 500, 500, 500, 500).expandTouchDelegate()

                }
                text = "1231231321"
                setOnClickListener {
                    Log.d("NestWithRecyclerViewActivity", "click")
                }
            })
        }
//        supportFragmentManager.beginTransaction().add(R.id.fl_fragment, RecyclerViewFragment.newInstance()).commit()
    }
}