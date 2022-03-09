package com.example.spandemo.nest

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spandemo.R


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
        view.findViewById<RecyclerView>(R.id.rv_item).apply {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            adapter = StringAdapter(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val visiblePos = lm.findFirstVisibleItemPosition()
                    if (visiblePos == 0) {
                        lm.findViewByPosition(visiblePos)?.let { view->
                            view.findViewById<ImageView>(R.id.iv_down_arrow)?.let {iv->
                                Log.d("RecyclerViewFragment", "findViewByPosition -- $iv --- $dy")
                            }
                        }
                    }
                }
            })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RecyclerViewFragment()
    }

    class StringAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        }

        class TopViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) 0 else 1
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//            Log.d("RecyclerViewFragment", "onCreateViewHolder")
            if (viewType == 0) {
                return TopViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_fragment_item_top_view, parent, false))
            }
            return ViewHolder(TextView(context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150)
                text = "12312"
            })
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//            Log.d("RecyclerViewFragment", "onBindViewHolder -- $position")
        }

        override fun getItemCount() = 200
    }
}