package com.example.spandemo.flex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.spandemo.R

class FlexboxActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flexbox)

        findViewById<RecyclerView>(R.id.rv_flex).run {

        }
    }

    class FlexboxAdapter : RecyclerView.Adapter<FlexboxAdapter.FlexboxRv>() {
        class FlexboxRv(private val view: View): ViewHolder(view) {
            fun a() {
                buildSpannedString {

                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlexboxRv {
            return FlexboxRv(View(parent.context))
        }

        override fun onBindViewHolder(holder: FlexboxRv, position: Int) {
        }

        override fun getItemCount(): Int {
            return 0
        }
    }
}