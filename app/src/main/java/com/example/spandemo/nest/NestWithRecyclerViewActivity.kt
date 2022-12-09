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
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentActivity
import com.example.spandemo.R
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class NestWithRecyclerViewActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest_with_recycler_view)
        val textView = findViewById<View>(R.id.text1)
        textView.post {

            findViewById<AppBarLayout>(R.id.appbar_layout).let {
                it.addOnOffsetChangedListener(AppBarLayout.BaseOnOffsetChangedListener<AppBarLayout> { appBarLayout, verticalOffset -> //                        //锚点位于顶部横向1/2, 纵向1/4位置
                    //                        textView.pivotX = textView.width.toFloat() / 2
                    textView.pivotY = textView.height.toFloat()

                    ((textView.height - abs(verticalOffset)) / textView.height.toFloat()).run {
                        textView.scaleX = this
                        textView.scaleY = this
                    }
                })
            }
        }

        supportFragmentManager.beginTransaction().add(R.id.fl_fragment, RecyclerViewFragment.newInstance()).commit()
    }
}