package com.example.spandemo.nest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.example.spandemo.R

class NestWithRecyclerViewActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nest_with_recycler_view)
        supportFragmentManager.beginTransaction().add(R.id.fl_fragment, RecyclerViewFragment.newInstance()).commit()
    }
}