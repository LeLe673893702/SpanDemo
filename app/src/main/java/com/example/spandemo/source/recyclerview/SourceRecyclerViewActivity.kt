package com.example.spandemo.source.recyclerview

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.spandemo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.reflect.Field

class SourceRecyclerViewActivity : AppCompatActivity() {
    private companion object {
        const val TAG = "SourceRecyclerViewActivity"
    }
    private val adapter by lazy {
        SourceRecyclerViewAdapter(this, MutableList(100) { "Tracker:$it" })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_source_recycler_view)
        findViewById<RecyclerViewWrapper>(R.id.rv_source).run {
            layoutManager = LinearLayoutManagerWrapper(context).apply {
                this.scrollListener = {
                    showMessage(this@run, "scollBy")
                }
            }
            adapter = this@SourceRecyclerViewActivity.adapter
            setLayoutListener(object : RecyclerViewWrapper.LayoutListener {
                override fun onBeforeLayout() {
                    try {
                        val mRecycler = Class.forName("androidx.recyclerview.widget.RecyclerView")
                            .getDeclaredField("mRecycler")
                        mRecycler.isAccessible = true
                        val recyclerInstance = mRecycler[this@run] as Recycler
                        val recyclerClass = Class.forName(mRecycler.type.name)
                        val mAttachedScrap = recyclerClass.getDeclaredField("mAttachedScrap")
                        mAttachedScrap.isAccessible = true
                        mAttachedScrap[recyclerInstance] =
                            ArrayListWrapper<RecyclerView.ViewHolder>()
                        val mAttached =
                            mAttachedScrap[recyclerInstance] as ArrayList<RecyclerView.ViewHolder>
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onAfterLayout() {
                    showMessage(this@run, "layout")
                }

            })
        }

        lifecycleScope.launchWhenStarted {
            withContext(Dispatchers.IO) {
                delay(10000)
            }
            adapter.datas[1] = "Tracker2222"
            adapter.notifyItemChanged(1)
        }
    }

    /**
     * 利用java反射机制拿到RecyclerView内的缓存并打印出来
     */
    private fun showMessage(rv: RecyclerViewWrapper, message: String) {
        try {
            val mRecycler: Field = Class.forName("androidx.recyclerview.widget.RecyclerView")
                .getDeclaredField("mRecycler")
            mRecycler.isAccessible = true
            val recyclerInstance = mRecycler.get(rv) as Recycler
            val recyclerClass = Class.forName(mRecycler.getType().getName())
            val mViewCacheMax = recyclerClass.getDeclaredField("mViewCacheMax")
            val mAttachedScrap = recyclerClass.getDeclaredField("mAttachedScrap")
            val mChangedScrap = recyclerClass.getDeclaredField("mChangedScrap")
            val mCachedViews = recyclerClass.getDeclaredField("mCachedViews")
            val mRecyclerPool = recyclerClass.getDeclaredField("mRecyclerPool")
            mViewCacheMax.isAccessible = true
            mAttachedScrap.isAccessible = true
            mChangedScrap.isAccessible = true
            mCachedViews.isAccessible = true
            mRecyclerPool.isAccessible = true
            val mViewCacheSize = mViewCacheMax.get(recyclerInstance) as Int
            val mAttached: ArrayListWrapper<RecyclerView.ViewHolder> =
                mAttachedScrap.get(recyclerInstance) as ArrayListWrapper<RecyclerView.ViewHolder>
            val mChanged = mChangedScrap.get(recyclerInstance) as? ArrayList<RecyclerView.ViewHolder>
            val mCached = mCachedViews.get(recyclerInstance) as ArrayList<RecyclerView.ViewHolder>
            val recycledViewPool = mRecyclerPool.get(recyclerInstance) as RecycledViewPool
            val recyclerPoolClass = Class.forName(mRecyclerPool.getType().getName())
            Log.e(
                TAG,
                """
                $message,
                mAttachedScrap（一缓） size is:${mAttached.maxSize}, current size is:${mAttached.size}, ${getAttachViewsInfo(mAttached)}, 
                mCachedViews（二缓） max size is:$mViewCacheSize, current size is: ${mCached.size}, ${getMCachedViewsInfo(mCached)}${
                    getRVPoolInfo(
                        recyclerPoolClass,
                        recycledViewPool
                    )
                }
                """.trimIndent()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getRVPoolInfo(aClass: Class<*>, recycledViewPool: RecycledViewPool): String {
        return try {
            val mScrapField: Field = aClass.getDeclaredField("mScrap")
            mScrapField.setAccessible(true)
            val mScrap = mScrapField.get(recycledViewPool) as SparseArray<*>
            val scrapDataClass =
                Class.forName("androidx.recyclerview.widget.RecyclerView\$RecycledViewPool\$ScrapData")
            val mScrapHeapField: Field = scrapDataClass.getDeclaredField("mScrapHeap")
            val mMaxScrapField: Field = scrapDataClass.getDeclaredField("mMaxScrap")
            mScrapHeapField.setAccessible(true)
            mMaxScrapField.setAccessible(true)
            var s = "\n mRecyclerPool（四缓） info:  "
            for (i in 0 until mScrap.size()) {
                val item = mScrapHeapField.get(mScrap.get(i)) as ArrayList<RecyclerView.ViewHolder>
                for (j in 0 until item.size) {
                    if (j == item.size - 1) {
                        s += ">>> "
                    } else if (j == 0) {
                        s += "mScrap[" + i + "] max size is:" + mMaxScrapField.get(mScrap.get(i))
                    }
                    s += """
                        mScrap[$i] 中的 mScrapHeap[$j] info is:${item[j]}
                        
                        """.trimIndent()
                }
            }
            s
        } catch (e: Exception) {
            e.printStackTrace()
            "  "
        }
    }

    private fun getAttachViewsInfo(viewHolders: ArrayList<RecyclerView.ViewHolder>): String {
        var i = 0
        val s = viewHolders.fold(StringBuilder("mAttachScrap (一缓) info: ")) { acc, viewHolder ->
            val itemView = viewHolder.itemView
            acc.append("mAttachScrap[${i++}] is ${itemView.findViewById<TextView>(R.id.string_item).text}")
            acc.append("\n")
        }
        return s.toString()
    }

    private fun getMCachedViewsInfo(viewHolders: ArrayList<RecyclerView.ViewHolder>): String {
        var s = "mCachedViews（二缓） info:  "
        if (viewHolders.size > 0) {
            var i = 0
            while (i < viewHolders.size) {
                val viewHolder = viewHolders[i]
                val itemView = viewHolder.itemView
                s += """
 mCachedViews[$i] is ${itemView.findViewById<TextView>(R.id.string_item).text}"""
                i++
            }

            // append
            if (i == 0) {
                s += "      "
            } else if (i == 1) {
                s += "    "
            } else if (i == 2) {
                s += "  "
            }
        } else {
            s += "      "
        }
        return """$s 
"""
    }
}