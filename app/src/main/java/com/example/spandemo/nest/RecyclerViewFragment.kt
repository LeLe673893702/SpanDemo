package com.example.spandemo.nest

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spandemo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


/**
 * A simple [Fragment] subclass.
 * Use the [RecyclerViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecyclerViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<HorizontalScrollView>(R.id.header_scroll_view).apply {

        }
        view.findViewById<RecyclerView>(R.id.rv_item).apply {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            lifecycleScope.launchWhenStarted {
                val a = async(Dispatchers.IO) {
//                    delay(10000)
                    MutableList(20, {"213"})
                }
                adapter = StringAdapter(context, a.await())
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visiblePos = lm.findFirstVisibleItemPosition()
                    if (visiblePos == 0) {
                        lm.findViewByPosition(visiblePos)?.let { view->
                            view.findViewById<ImageView>(R.id.iv_down_arrow)?.let {iv->
                                val drawable = ContextCompat.getDrawable(context, R.drawable.gd_preview_unselect_indicator_drawable)
                                Log.d("RecyclerViewFragment", "findViewByPosition -- $iv --- $dy")
                            }
                        }
                    }
                }
            })

//            lifecycleScope.launchWhenStarted {
//                withContext(Dispatchers.IO) {
//                    delay(15000)
//                }
//
//                val removeList = datas.subList(10, datas.size)
//                val size = removeList.size
//                datas.removeAll(removeList)
//                (adapter as StringAdapter).notifyItemRangeChanged(10, size)
//            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RecyclerViewFragment()
    }

    class StringAdapter(private val context: Context, var datas: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        }

        class TopViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }

        override fun getItemViewType(position: Int): Int {
            return 0
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//            Log.d("RecyclerViewFragment", "onCreateViewHolder")
//            if (viewType == 0) {
//                return TopViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_fragment_item_top_view, parent, false))
//            }
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.string_item_layout, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//            Log.d("RecyclerViewFragment", "onBindViewHolder -- $position")
            holder.itemView.findViewById<TextView>(R.id.string_item).apply {
                text = "Track $position"
            }
        }

        override fun getItemCount() = datas.size
    }
}