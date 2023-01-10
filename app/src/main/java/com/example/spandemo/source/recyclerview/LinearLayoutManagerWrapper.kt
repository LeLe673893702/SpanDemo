package com.example.spandemo.source.recyclerview

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearLayoutManagerWrapper(context: Context?) : LinearLayoutManager(context) {
    var scrollListener: (() -> Unit)? = null
    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
//        Log.e("SourceRecyclerViewActivity", "scrollVerticallyBy")

        val a = super.scrollVerticallyBy(dy, recycler, state)
        scrollListener?.invoke()
        return a
    }

}