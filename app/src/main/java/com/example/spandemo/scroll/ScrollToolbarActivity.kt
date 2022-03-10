package com.example.spandemo.scroll

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spandemo.R

class ScrollToolbarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_toolbar)
        val datas = mutableListOf<String>()
        repeat(100) {
            datas.add("123131")
        }
        findViewById<RecyclerView>(R.id.rv_data).run {
            val sa = ScrollAdapter(context, datas)
            adapter = sa
            val lm = LinearLayoutManager(context).apply {
                addOnScrollListener(object : RecyclerView.OnScrollListener() {

                })
            }
            layoutManager = lm
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val firstVisiblePos = lm.findFirstVisibleItemPosition()
                    if (firstVisiblePos == 0) {
                        val view = lm.findViewByPosition(0)
                        Log.d("ScrollToolbarActivity", view.toString())
                    }
                }
            })
        }
    }
}

class ScrollAdapter(val context: Context, private var datas: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == 0) ViewHolder1(AutoTextView(context)) else ViewHolder2(TextView(context).apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.teal_700))
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120)
        })
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ViewHolder2)?.setData(datas[position])
        (holder as? ViewHolder1)?.setData(datas[position])
    }

    override fun getItemCount(): Int = 50

    class ViewHolder1(private val view: TextView) : RecyclerView.ViewHolder(view) {
        fun setData(data: String) {
            view.text = data
        }
    }

    class ViewHolder2(private val view: TextView) : RecyclerView.ViewHolder(view) {
        fun setData(data: String) {
            view.text = data
        }
    }

}

class AutoTextView(context: Context, attrs: AttributeSet? = null):
    androidx.appcompat.widget.AppCompatTextView(context, attrs) {
        init {
            setBackgroundColor(ContextCompat.getColor(context, R.color.teal_200))
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)
            text = "顶部区域"
            setTextColor(ContextCompat.getColor(context, R.color.white))
        }
}