package com.example.spandemo.flex

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.math.max

class WordCloudLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    // 达到指定数量换行
    private val wrapCount = 4
    // 最大换行书
    private val maxLineCount = 2
    private val lineHeights = mutableListOf<Int>()
    private var allViews : MutableList<MutableList<View>>?= null
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var parentHeight = 0
        var parentWidth = 0

        var lineHeight = 0
        var lineWidth = 0

        allViews = MutableList(maxLineCount) {
            mutableListOf()
        }

        // 单行计算
        if (childCount < wrapCount) {
            children.forEach {view->
                measureChildWithMargins(view, widthMeasureSpec, 0, heightMeasureSpec, 0)
                val (childMeasuredWidth, childMeasuredHeight) = getChildMeasureSize(view)
                // 一行宽度累加
                parentWidth += childMeasuredWidth
                // 一行高度取最大
                parentHeight = max(parentHeight, childMeasuredHeight)
            }
            allViews?.add(children.toMutableList())
            setMeasuredDimension(parentWidth + paddingLeft + paddingRight,
                parentHeight + paddingTop + paddingBottom)
            return
        }

        // 多行按照竖向顺序排列
        /**
         *
         *  {1, 2, 3, 4, 5, 6}
         *  TO
         *  {1, 3, 5}
         *  {2, 4, 6}
         */
        children.filterIndexed { index, _ -> index < maxLineCount * wrapCount }
            .flatMapIndexed { index: Int, view: View ->
                allViews?.get(index % maxLineCount)?.add(view)
                allViews!!
            }.forEach { lineViews ->
                lineViews.forEach {  view->
                    measureChildWithMargins(view, widthMeasureSpec, 0, heightMeasureSpec, 0)
                    val (childMeasuredWidth, childMeasuredHeight) = getChildMeasureSize(view)

                    // 一行宽度累加
                    lineWidth += childMeasuredWidth
                    // 一行高度取最大
                    lineHeight = max(lineHeight, childMeasuredHeight)
                }
                // 换行高度累加
                parentHeight += lineHeight
                // 换行取最大宽度
                parentWidth = max(parentWidth, lineWidth)
                lineHeights.add(lineHeight)

                // 重置
                lineHeight = 0
                lineWidth = 0
            }

        setMeasuredDimension(parentWidth + paddingLeft + paddingRight,
        parentHeight + paddingTop + paddingBottom)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (allViews.isNullOrEmpty()) {
            return
        }
        // 单行按照横向顺序排列
        if ((allViews?.size?:0) < 2){
            allViews?.forEachIndexed {lineIndex, lineViews->
                val left = l + paddingLeft
                lineViews.forEach { view ->
                    view.layout(left, top, left + view.measuredWidth, b + lineHeights[lineIndex])
                }
            }
            return
        }


        allViews?.let {
            var top = t + paddingTop
            it.forEachIndexed { lineIndex, lineViews ->
                // 换行 left 重置
                var left = l + paddingLeft
                lineViews.forEachIndexed { index, view ->
                    val currentLp = view.layoutParams as MarginLayoutParams
                    val lv = left + currentLp.leftMargin
                    val rv = lv  + view.measuredWidth
                    val tv = top + currentLp.topMargin
                    val bv = tv + view.measuredHeight
                    view.layout(lv, tv, rv, bv)
                    left = rv + currentLp.rightMargin
                }
                // 换行高度增加
                top += lineHeights[lineIndex]
            }
        }
    }

    private fun getChildMeasureSize(childView: View?): Pair<Int, Int> {
        if (childView == null) return Pair(0, 0)
        val lp = childView.layoutParams as MarginLayoutParams
        val childWidth = childView.measuredWidth + lp.leftMargin + lp.rightMargin
        val childHeight = childView.measuredHeight + lp.topMargin + lp.bottomMargin
        return Pair(childWidth, childHeight)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT)
    }
}