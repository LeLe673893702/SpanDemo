package com.example.spandemo

import android.content.Context
import android.util.AttributeSet
import android.util.SparseBooleanArray
import android.view.View
import android.widget.ImageView


/*
* 攻略、问答tap共用标签flowlayout
*/
class HotKeysFlowLayout(context: Context, attrs: AttributeSet) : TapFlowLayoutV2(context, attrs) {
    init {
        defaultLine = 1
        maxLine = defaultLine
        biggestLine = 6

        expandView = ImageView(context).apply {
            setImageResource(R.drawable.gd_ic_hot_keys_down)
            scaleType = ImageView.ScaleType.CENTER
            layoutParams = MarginLayoutParams(
                DensityUtils.dpToPx(30),
                DensityUtils.dpToPx(30)
            ).apply {
                context.getString(R.string.app_info)
                setMargins(
                    DensityUtils.dpToPx(4),
                    DensityUtils.dpToPx(6),
                    DensityUtils.dpToPx(4),
                    DensityUtils.dpToPx(6)
                )
            }
        }
        expandView?.setOnClickListener {
            onExpandClickListener?.onClick(it, maxLine < biggestLine)
            if (!isExpand) {
                expandView?.rotateAnimate(180f, 200)
                maxLine = biggestLine
                isExpand = true
                requestLayout()
                expandAll()

            } else {
                maxLine = defaultLine

                expandView?.rotateAnimate(-180f, 200)
                shrink{
                    requestLayout()
                }
            }
        }
    }

    override fun setTagAdapter(adapter: BaseFlowAdapter<*>) {
        this.adapter = adapter
        viewHistory = SparseBooleanArray(adapter.getCount())
        changeAdapter()
    }


    var onExpandClickListener: OnExpandClickListener? = null

    interface OnExpandClickListener {
        fun onClick(view: View?, isExpand: Boolean)
    }


}