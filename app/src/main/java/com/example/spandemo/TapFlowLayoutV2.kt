package com.example.spandemo

import android.content.Context
import android.util.AttributeSet
import android.util.LayoutDirection
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import androidx.core.text.TextUtilsCompat
import androidx.core.view.allViews
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author yuepeng
 * @description: 后置展开功能的流布局
 * @date :2021/9/13 3:39 下午
 */
open class TapFlowLayoutV2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    val TAG = "TapFlowLayoutV2"
    val LEFT = -1
    val CENTER = 0
    val RIGHT = 1

    //特别注意，只有在每一行高度都相同时，才能使用这个函数
    var maxLine = -1

    //标记是否高度超过了行数限制
    var isOverHeight = false
    var isExpand = false

    //设置添加到第n个tag后，强制换一行
    val changeLinePosition = -1

    val mAllViews: MutableList<MutableList<View?>> = ArrayList()
    val mLineHeight: MutableList<Int> = ArrayList()
    val mLineWidth: MutableList<Int> = ArrayList()
    var mGravity = LEFT
    var lineViews: MutableList<View?> = ArrayList()
    var flowOriginHeight = 0
    var expandView: View? = null
    var biggestLine = 3 //最大3行
    var defaultLine = 1 //默认显示行数
    lateinit var adapter: BaseFlowAdapter<*>
    var onTagClickListener: OnTagClickListener? = null
    var onTagViewListener: OnTagViewListener? = null
    var viewHistory: SparseBooleanArray? = null

    private var allHeight = 0
    private var  shrinkHeight = 0

    var calcWidth = 0
    var calcHeight = 0

    private var isFirstExpand = false

    public interface OnTagClickListener {
        fun onTagClick(view: View?, position: Int, parent: TapFlowLayoutV2?): Boolean
    }

    public interface OnTagViewListener {
        fun onTagView(view: View?, position: Int)
    }

    init {
        val layoutDirection = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault())
        if (layoutDirection == LayoutDirection.RTL) {
            mGravity = if (mGravity == LEFT) {
                RIGHT
            } else {
                LEFT
            }
        }
    }

    fun setGravity(mGravity: Int) {
        this.mGravity = mGravity
    }
    private var isCalc = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)
        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)
        expandView?.let {
            measureChild(expandView, widthMeasureSpec, heightMeasureSpec)
        }
        expandView?.parent?.let {
            (it as? ViewGroup)?.removeView(expandView)
        }

        // wrap_content

        var lineWidth = 0
        var lineHeight = 0
        val cCount: Int = childCount
        calcHeight = 0
        calcWidth = 0
        var hasAddExpand = false


        // 测量前初始化相关的容器
        mAllViews.clear()
        mLineHeight.clear()
        mLineWidth.clear()
        lineViews.clear()

        for (i in 0 until cCount) {
            val child: View = getChildAt(i)
            if (child.visibility == GONE) {
                continue
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val (childWidth, childHeight) = getChildMeasureSize(child)

            //宽度不够换行，或者达到换行的position 强行换一行 i == changeLinePosition
            if (lineWidth + childWidth > sizeWidth - paddingLeft - paddingRight|| i == changeLinePosition) {
                mLineHeight.add(lineHeight)
                mAllViews.add(lineViews)
                mLineWidth.add(lineWidth)

                calcWidth = Math.max(calcWidth, lineWidth)
                lineWidth = childWidth
                calcHeight += lineHeight
                lineHeight = Math.max(lineHeight, childHeight)
                lineViews = arrayListOf(child)
            } else {
                lineWidth += childWidth
                lineHeight = Math.max(lineHeight, childHeight)
                lineViews.add(child)
            }

            if (!isCalc) {
                calcAddExpandView(sizeWidth)
            }


            // 第一个超过最大行数-1就退出，防止+expandView会多换一行
            if (mAllViews.size >= biggestLine - 1) {
                break
            }
        }

        // 最后一行需要再次计算
        calcWidth = Math.max(lineWidth, calcWidth)
        calcHeight += lineHeight
        mLineHeight.add(lineHeight)
        mAllViews.add(lineViews)
        mLineWidth.add(lineWidth)

        calcAddExpandView(sizeWidth)

        if (!isExpand) {
            shrinkHeight = mLineHeight.take(maxLine).sum() + paddingTop + paddingBottom
            allHeight = getWrapNextRowHeight(sizeWidth, calcHeight + paddingTop + paddingBottom)
        }

        flowOriginHeight = calcHeight
        if (maxLine > 0 && lineHeight > 0 && calcHeight > maxLine * lineHeight) {
            isOverHeight = true
            calcHeight = maxLine * lineHeight
        }

        setMeasuredDimension(
            if (modeWidth == MeasureSpec.EXACTLY) sizeWidth else calcWidth + paddingLeft + paddingRight,
            if (modeHeight == MeasureSpec.EXACTLY) sizeHeight else calcHeight + paddingTop + paddingBottom
        )
    }

    private fun calcAddExpandView(sizeWidth: Int) {
        val (expandWith, expandHeight) = expandView?.run {
            getChildMeasureSize(this)
        } ?: kotlin.run { Pair(0,0) }
        //二次计算，处理末尾需要增加展开标识 如果当前可以画完所有的内容则不添加展开标识
        if ((isExpand || mAllViews.size > maxLine) && expandView != null) {
            //在当前最大展示行数的末尾增加操作组件
            val index = (mAllViews.size - 1).coerceAtMost(maxLine - 1)
            // 添加展开按钮所在行
            val addExpandViewInRows = mAllViews[index]
            val addExpandViewInRowWidth = mLineWidth[index]

            // 判断添加展开按钮，是否需要换行
            if (addExpandViewInRowWidth + expandWith > sizeWidth - paddingLeft - paddingRight) {
                if (addExpandViewInRows.size == 1) {
                    //如果添加展开按钮的那行只有1个item，则不换行，改变它的大小，将展开按钮放入
                    addExpandViewInRows.firstOrNull()?.let {
                        measureChild(
                            it,
                            MeasureSpec.makeMeasureSpec(sizeWidth - expandWith, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(mLineHeight[index], MeasureSpec.EXACTLY)
                        )
                    }
                    addExpandViewInRows.add(expandView)
                } else {
                    // 如果有多个，该行最后一个 + expandView， 一起换行
                    addExpandViewInRows.lastOrNull()?.let { lastViewInRow ->
                        val (childWidth,childHeight) = getChildMeasureSize(lastViewInRow)
                        // 添加后仍然超出最大宽度，不需要再换行，直接改变大小
                        if (childWidth + expandWith > sizeWidth - paddingLeft - paddingRight) {
                            measureChild(
                                lastViewInRow,
                                MeasureSpec.makeMeasureSpec(
                                    sizeWidth - expandWith,
                                    MeasureSpec.EXACTLY
                                ),
                                MeasureSpec.makeMeasureSpec(mLineHeight[index], MeasureSpec.EXACTLY)
                            )
                        }
                        // 最后一个view的宽高可能发生变化，需要重新计算
                        val (newChildWidth, newChildHeight) = getChildMeasureSize(lastViewInRow)
//                        if (index < maxLine - 1) {
                            // 由于最后一个item从该行移除，需要更新数据
                            addExpandViewInRows.remove(lastViewInRow)
                            mLineWidth.set(index, mLineWidth[index] - newChildWidth)
                            mLineHeight.set(index, Math.min(mLineHeight[index], newChildHeight))

                            // 另起一行
                            val expandLines = mutableListOf<View?>(lastViewInRow, expandView!!)

                            // 更新
                            mLineWidth.add(newChildWidth + expandWith)
                            mLineHeight.add(Math.max(newChildHeight, expandHeight))
                            mAllViews.add(expandLines)
                            calcWidth = Math.max(mLineWidth.last(), width)
                            calcHeight += mLineHeight.last()
                            maxLine += 1
//                        } else {

//                        }

                    }
                }
            } else {
                addExpandViewInRows.add(expandView)
                mLineWidth[index] = expandWith + mLineWidth[index]
            }

            addView(expandView)

        }
    }

    private fun getWrapNextRowHeight(sizeWidth: Int, curHeight: Int):Int {
        val (expandWith, expandHeight) = expandView?.run {
            getChildMeasureSize(this)
        } ?: kotlin.run { Pair(0,0) }
        val index = (mAllViews.size - 1)
        // 添加展开按钮所在行
        val addExpandViewInRows = mAllViews[index]
        val addExpandViewInRowWidth = mLineWidth[index]
        // 判断添加展开按钮，是否需要换行
        if (addExpandViewInRowWidth + expandWith > sizeWidth - paddingLeft - paddingRight && addExpandViewInRows.size > 1) {
            return curHeight + Math.max(addExpandViewInRows.last()?.measuredHeight?:0, expandHeight)
        }
        return curHeight
    }

    private fun getChildMeasureSize(childView: View): Pair<Int, Int> {
        val lp = childView.layoutParams as MarginLayoutParams
        val childWidth = childView.measuredWidth + lp.leftMargin + lp.rightMargin
        val childHeight = childView.measuredHeight + lp.topMargin + lp.bottomMargin
        return Pair(childWidth, childHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // 测量前初始化相关的容器

//        expandView?.parent?.let {
//            (it as? ViewGroup)?.removeView(expandView)
//        }
        val width: Int = width

        var lineHeight = 0

        val expandWith = expandView?.run {
            val eLps = layoutParams as MarginLayoutParams
            measuredWidth + eLps.leftMargin + eLps.rightMargin
        } ?: kotlin.run { 0 }
//        for (i in 0 until cCount) {
//            val child: View = getChildAt(i)
//            if (child.visibility == GONE) continue
//            val lp = child
//                .layoutParams as MarginLayoutParams
//            val childWidth = child.measuredWidth
//            val childHeight = child.measuredHeight
//            //计算最大行数的可换行长度 需要扣除展开组件 如果最后一个位置可以放下则不添加展开按钮
//            if (hasAddExpand.not() && (mAllViews.size == maxLine - 1)) {
//                lineWidth += expandWith
//                hasAddExpand = true
//            }
//
//            if(hasAddExpand.not() && i==cCount-1){
//                lineWidth += expandWith
//                hasAddExpand = true
//            }
//
//            //宽度够换行，或者达到换行的position 强行换一行 i == changeLinePosition
//            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - paddingLeft - paddingRight || i == changeLinePosition) {
//                //1行的时候如果刚好可以放下则不需要换行和添加展开按钮
//                if (i == cCount - 1 && childWidth + lineWidth - expandWith + lp.leftMargin + lp.rightMargin <= width && maxLine == defaultLine && i != changeLinePosition) {
//                    lineViews.add(child)
//                    continue
//                }
//                //处理第一个过长的问题
//                if (i == 0) {
//                    measureChild(
//                        child,
//                        MeasureSpec.makeMeasureSpec(width - expandWith, MeasureSpec.EXACTLY),
//                        MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
//                    )
//                    lineWidth += childWidth + lp.leftMargin + lp.rightMargin
//                    lineHeight = Math.max(
//                        lineHeight, childHeight + lp.topMargin
//                                + lp.bottomMargin
//                    )
//                    lineViews.add(child)
//                    continue
//                }
//
//                mLineHeight.add(lineHeight)
//                mAllViews.add(lineViews)
//                mLineWidth.add(lineWidth)
//
//                lineWidth = 0
//                lineHeight = childHeight + lp.topMargin + lp.bottomMargin
//                lineViews = ArrayList()
//            }
//
//            lineWidth += childWidth + lp.leftMargin + lp.rightMargin
//            lineHeight = Math.max(
//                lineHeight, childHeight + lp.topMargin
//                        + lp.bottomMargin
//            )
//            lineViews.add(child)
//        }

//        mLineHeight.add(lineHeight)
//        mLineWidth.add(lineWidth)
//        mAllViews.add(lineViews)
        //处理末尾需要增加展开标识 如果当前可以画完所有的内容则不添加展开标识
//        if ((isExpand || mAllViews.size > maxLine) && expandView != null) {
//            //在当前最大展示行数的末尾增加操作组件
//            val index = (mAllViews.size - 1).coerceAtMost(maxLine - 1)
//            val addList = mAllViews[index]
//            //处理如果等待添加展开组件的当前行内容超过预定超过预定长度，需要重新设置他的宽度
//            if (addList.size == 1) {
//                addList.firstOrNull()?.let {
//                    measureChild(
//                        it,
//                        MeasureSpec.makeMeasureSpec(width - expandWith, MeasureSpec.EXACTLY),
//                        MeasureSpec.makeMeasureSpec(mLineHeight[index], MeasureSpec.EXACTLY)
//                    )
//                }
//                addList.add(expandView)
//            } else {
//                addList.add(expandView)
//            }
//            addView(expandView)
//        }
        var left: Int = paddingLeft
        var top: Int = paddingTop
        val lineNum = mAllViews.size
        val lastWith = width - expandWith
        for (i in 0 until lineNum) {
            lineViews = mAllViews[i]
            lineHeight = mLineHeight[i]

            // set gravity
            val currentLineWidth: Int = this.mLineWidth[i]
            when (this.mGravity) {
                LEFT -> left = paddingLeft
                CENTER -> left = (width - currentLineWidth) / 2 + paddingLeft
                RIGHT -> {
                    //  适配了rtl，需要补偿一个padding值
                    left = width - (currentLineWidth + paddingLeft) - paddingRight
                    //  适配了rtl，需要把lineViews里面的数组倒序排
                    lineViews.reverse()
                }
            }
            for (j in lineViews.indices) {
                val child = lineViews[j]
                if (child!!.visibility == GONE) {
                    continue
                }
                val lp = child
                    .layoutParams as MarginLayoutParams
                val lc = left + lp.leftMargin
                val tc = top + lp.topMargin
                val bc = tc + child.measuredHeight
                val rc = if (lineViews.size == 2 && lineViews.lastOrNull() !is TagView && j == 0) {
                    lc + lastWith
                } else {
                    lc + child.measuredWidth
                }
                child.tag = i > defaultLine - 1
                child.layout(lc, tc, rc, bc)
                left += (child.measuredWidth + lp.leftMargin
                        + lp.rightMargin)
            }
            if (i == maxLine - 1) {
                break
            }
            top += lineHeight
        }
    }


    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        return MarginLayoutParams(getContext(), attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams? {
        return MarginLayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams? {
        return MarginLayoutParams(p)
    }

    open fun setTagAdapter(adapter: BaseFlowAdapter<*>) {
        this.adapter = adapter
        viewHistory = SparseBooleanArray(adapter.getCount())
        changeAdapter()
    }


    protected fun changeAdapter() {
        removeAllViews()
        var tagViewContainer: View? = null
        for (i in 0 until adapter.getCount()) {
            val tagView = adapter.getView(this, i)
            tagViewContainer = TagView(context)
            tagView.isDuplicateParentStateEnabled = true
            if (tagView.layoutParams == null) {
                val lp = MarginLayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(
                    DensityUtils.dpToPx(5),
                    DensityUtils.dpToPx(5),
                    DensityUtils.dpToPx(5),
                    DensityUtils.dpToPx(5)
                )
                tagViewContainer.layoutParams = lp
            }
            tagViewContainer.addView(tagView, tagView.layoutParams)
            addView(tagViewContainer)
            tagView.isClickable = false
            tagViewContainer.setOnClickListener {
                onTagClickListener?.onTagClick(it, i, this)
            }
        }
    }

    fun expandAll(finishCall: (() -> Unit)? = null) {
        heightAnimator(shrinkHeight, allHeight) {
            isExpand = true
            finishCall?.invoke()
        }
    }

    fun getSingleLineHeight(): Int {
        return if (mLineHeight.isEmpty()) 0 else mLineHeight[0]
    }

    private fun getMaxHeight() =
        if (flowOriginHeight >= (biggestLine * getSingleLineHeight())) biggestLine * getSingleLineHeight() else flowOriginHeight

    fun shrink(finishCall: (() -> Unit)? = null) {
        heightAnimator(allHeight, shrinkHeight) {
            isExpand = false
            finishCall?.invoke()
        }
    }
    open fun reset(){
        layoutParams.height = getSingleLineHeight() * defaultLine
        isExpand = false
        requestLayout()
    }
}
