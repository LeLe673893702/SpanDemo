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



        // 单行计算
        allViews = mutableListOf()
        if (childCount <= wrapCount) {
            allViews?.add(children.toMutableList())
            // 测量单行
            val (lineWidth, lineHeight) = measureColumnView(
                allViews!!.first(),
                widthMeasureSpec,
                heightMeasureSpec
            )
            setMeasuredDimension(lineWidth + paddingLeft + paddingRight,
                lineHeight + paddingTop + paddingBottom)
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

        var parentHeight = 0
        var parentWidth = 0
        allViews = MutableList(maxLineCount) {
            mutableListOf()
        }
        children.filterIndexed { index, _ -> index < maxLineCount * wrapCount }
            .flatMapIndexed { index: Int, view: View ->
                allViews?.get(index % maxLineCount)?.add(view)
                allViews!!
            }.forEach { lineViews ->
                // 测量单行
                val (lineWidth, lineHeight) = measureColumnView(
                    lineViews,
                    widthMeasureSpec,
                    heightMeasureSpec
                )
                // 换行高度累加
                parentHeight += lineHeight
                // 换行取最大宽度
                parentWidth = max(parentWidth, lineWidth)
                lineHeights.add(lineHeight)
            }

        setMeasuredDimension(parentWidth + paddingLeft + paddingRight,
        parentHeight + paddingTop + paddingBottom)
    }

    /**
     * 测量单行
     */
    private fun measureColumnView(
        lineViews: MutableList<View>,
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ): Pair<Int, Int> {
        // 重置
        var lineHeight = 0
        var lineWidth = 0
        lineViews.forEach { view ->
            measureChildWithMargins(view, widthMeasureSpec, 0, heightMeasureSpec, 0)

            val (childMeasuredWidth, childMeasuredHeight) = getChildMeasureSize(view)

            // 一行宽度累加
            lineWidth += childMeasuredWidth
            // 一行高度取最大
            lineHeight = max(lineHeight, childMeasuredHeight)
        }
        return Pair(lineWidth, lineHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (allViews.isNullOrEmpty()) {
            return
        }
        // 单行按照横向顺序排列
        if (childCount <= wrapCount){
            layoutColumnView(l + paddingLeft, allViews!!.first(), t + paddingTop)
            return
        }

        allViews?.let {
            var childTop = t + paddingTop
            it.forEachIndexed { lineIndex, lineViews ->
                childTop = layoutColumnView(l + paddingLeft, lineViews, childTop)
                // 换行高度增加
                childTop += lineHeights[lineIndex]
            }
        }
    }

    /**
     * 放置单行view
     */
    private fun layoutColumnView(
        initLeft: Int,
        lineViews: MutableList<View>,
        initTop: Int
    ): Int {
        // 换行 left 重置
        var childLeft = initLeft
        var lp: MarginLayoutParams?=null
        lineViews.forEachIndexed { index, view ->
            lp = view.layoutParams as MarginLayoutParams

            childLeft += lp!!.leftMargin
            val childRight = childLeft + view.measuredWidth
            val childTop = initTop + lp!!.topMargin
            val childBottom = childTop + view.measuredHeight

            view.layout(childLeft, childTop, childRight, childBottom)

            // 递增
            childLeft = childRight + lp!!.rightMargin
        }

        return initTop + (lp?.bottomMargin?:0)
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