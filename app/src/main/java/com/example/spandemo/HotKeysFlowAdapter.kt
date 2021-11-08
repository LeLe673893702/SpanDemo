package com.example.spandemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class HotKeysFlowAdapter(private val context: Context,
                         hotKeys: List<HotKeysWrapper.HotKey>): BaseFlowAdapter<HotKeysWrapper.HotKey>(hotKeys) {
    override fun getView(parent: TapFlowLayoutV2, position: Int): View {
       val view =  LayoutInflater.from(context).inflate(R.layout.gd_guide_hot_keys_item_view, null, false)
        view.layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            DensityUtils.dpToPx(30)).apply {
                setMargins(
                    DensityUtils.dpToPx(4),
                    DensityUtils.dpToPx(6),
                    DensityUtils.dpToPx(4),
                    DensityUtils.dpToPx(6),
                )
        }
        val tvLabel = view.findViewById<TextView>(R.id.tvLabel)
        tvLabel?.let { it.text = getItem(position).displayText }
        return view
    }
}