package com.example.spandemo

import android.view.View

/**
 * @author yuepeng
 * @description:
 * @date :2021/9/13 4:50 下午
 */
abstract class BaseFlowAdapter<T>(var datas: List<T>) {
    private var mOnDataChangedListener: OnDataChangedListener? = null

    interface OnDataChangedListener {
        fun onChanged()
    }

    open fun setOnDataChangedListener(listener: OnDataChangedListener?) {
        mOnDataChangedListener = listener
    }


    open fun getCount(): Int {
        return datas.size
    }

    open fun notifyDataChanged() {
        mOnDataChangedListener?.onChanged()
    }

    open fun getItem(position: Int): T {
        return datas[position]
    }

    abstract fun getView(parent: TapFlowLayoutV2, position: Int): View
}