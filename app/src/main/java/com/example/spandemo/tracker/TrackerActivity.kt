package com.example.spandemo.tracker

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import com.example.spandemo.R
import com.example.spandemo.nest.RecyclerViewFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class TrackerActivity: AppCompatActivity() {
    companion object {
        const val TAG = "TrackerActivity"
    }
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_recycler_view)
        recyclerView = findViewById<RecyclerView>(R.id.rv_item).apply {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            val datas = MutableList(200) { "213" }
            val stringAdapter = RecyclerViewFragment.StringAdapter(context, datas)
            adapter = stringAdapter

            lifecycleScope.launchWhenStarted {
                withContext(Dispatchers.IO) {
                    delay(5000)
                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "-------remove first item------------")
                        datas.removeAt(0)
                        stringAdapter.notifyItemRemoved(0)
                    }

                    delay(3000)

                    withContext(Dispatchers.Main) {
                        Log.d(TAG, "-------remove second item------------")
                        datas.removeAt(0)
                        stringAdapter.notifyItemChanged(0)
                    }
                }
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
//                    Log.d(TAG, "onScrolled, dx:$dx, dy:$dy")
//                    processChangedEventWithDetachedView(null, "onScrolled")
                }
            })

            addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                Log.d(TAG, "OnLayoutChange: view:$v, top:$top, bottom:$bottom, oldTop:$oldTop, oldBottom:$oldBottom")
//                processChangedEventWithDetachedView(null, "OnLayoutChange")
            }

            addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
//                    Log.d(TAG, "onChildViewAttachedToWindow---view:$view")
                }

                override fun onChildViewDetachedFromWindow(view: View) {
//                    Log.d(TAG, "onChildViewDetachedFromWindow---view:$view")
                }

            })
        }
    }

    private fun processChangedEventWithDetachedView(detachedView: View?, debug: String) {
        // Process all attached children
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            if (child != null && child !== detachedView) {
                Log.d(TAG, "processChangedChild:$child, debug:$debug")
            }
        }
    }
}