package com.example.spandemo

import android.content.Context
import android.util.AttributeSet
import android.util.LayoutDirection
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import androidx.core.text.TextUtilsCompat
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
    var lineViews: MutableList<View?> = ArrayList()

    val tmpAllViews: MutableList<MutableList<View?>> = ArrayList()
    val tmpLineHeight: MutableList<Int> = ArrayList()
    val tmpLineWidth: MutableList<Int> = ArrayList()
    var tmpLineViews: MutableList<View?> = ArrayList()
    var mGravity = LEFT
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


        val cCount: Int = childCount
        calcHeight = 0
        calcWidth = 0
        var hasAddExpand = false

        calcAllViews(widthMeasureSpec, heightMeasureSpec, sizeWidth)

        if (!isExpand) {
            allHeight = calcHeight
        }

        calcHeight = 0
        calcWidth = 0
        // 测量前初始化相关的容器
        mAllViews.clear()
        mLineHeight.clear()
        mLineWidth.clear()
        lineViews.clear()

        val (expandWith, expandHeight) = expandView?.run {
            getChildMeasureSize(this)
        } ?: kotlin.run { Pair(0,0) }
        //二次计算，处理末尾需要增加展开标识 如果当前可以画完所有的内容则不添加展开标识
        var count = 0
        if ((isExpand || tmpAllViews.size > maxLine) && expandView != null) {
            val addExpandViewLine = (tmpAllViews.size).coerceAtMost(maxLine)
            for (line in 0 until addExpandViewLine) {
                var lineWidth = 0
                var lineHeight = 0
                // 没有到添加expandView所在行
                if (line < addExpandViewLine - 1) {
                    count += tmpAllViews[line].size
                    getFormTmpViews(line)
                    lineWidth = tmpLineWidth[line]
                    lineHeight = tmpLineHeight[line]
                } else {
                    // 遍历
                    val addExpandViewLines = mutableListOf<View?>()
                    val lastLines = tmpAllViews[addExpandViewLine-1]
                   for (i in 0 until  lastLines.size) {
                       val (childW, childH) = getChildMeasureSize(lastLines[i])
                       // 如果当前view + expandView 超过边界
                       if (lineWidth + childW + expandWith > sizeWidth - paddingLeft - paddingRight) {
                           // 如果view的宽度是expandView的两倍则缩小view的宽度，再添加
                           // 反之，则不添加view，留位置给expandViews
                           if (2 * expandWith <= childW) {
                               measureChild(
                                   lastLines[i],
                                   MeasureSpec.makeMeasureSpec(childW - expandWith, MeasureSpec.EXACTLY),
                                   MeasureSpec.makeMeasureSpec(childH, MeasureSpec.EXACTLY)
                               )
                               addExpandViewLines.add(lastLines[i])
                               val (newChildW, newChildH) =  getChildMeasureSize(lastLines[i])
                               lineWidth += newChildW
                               lineHeight = Math.max(newChildH, lineHeight)
                               count++
                           }
                           break
                       } else {
                           addExpandViewLines.add(lastLines[i])
                           lineWidth += childW
                           lineHeight = Math.max(childH, lineHeight)
                           count++
                       }
                   }
                    addExpandViewLines.add(expandView)
                    lineWidth += expandWith
                    lineHeight = Math.max(expandHeight, lineHeight)
                    mAllViews.add(addExpandViewLines)
                    mLineWidth.add(lineWidth)
                    mLineHeight.add(lineHeight)
                    addView(expandView)
                }

                // 最后一行需要再次计算
                calcWidth = Math.max(lineWidth, calcWidth)
                calcHeight += lineHeight
            }
        }

        if (!isExpand) {
            shrinkHeight = calcHeight
        }

        setMeasuredDimension(
            if (modeWidth == MeasureSpec.EXACTLY) sizeWidth else calcWidth + paddingLeft + paddingRight,
            if (modeHeight == MeasureSpec.EXACTLY) sizeHeight else calcHeight + paddingTop + paddingBottom
        )
    }

    private fun getFormTmpViews(line: Int) {
        mAllViews.add(tmpAllViews[line])
        mLineWidth.add(tmpLineWidth[line])
        mLineHeight.add(tmpLineHeight[line])
    }

    private fun calcAllViews(widthMeasureSpec: Int, heightMeasureSpec: Int, sizeWidth: Int) {
        tmpAllViews.clear()
        tmpLineHeight.clear()
        tmpLineWidth.clear()
        tmpLineViews.clear()
        var lineWidth = 0
        var lineHeight = 0
        val cCount: Int = childCount
        calcHeight = 0
        calcWidth = 0
        for (i in 0 until cCount) {
            val child: View = getChildAt(i)
            if (child.visibility == GONE) {
                continue
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val (childWidth, childHeight) = getChildMeasureSize(child)

            //宽度不够换行，或者达到换行的position 强行换一行 i == changeLinePosition
            if (lineWidth + childWidth > sizeWidth - paddingLeft - paddingRight|| i == changeLinePosition) {
                tmpLineHeight.add(lineHeight)
                tmpAllViews.add(tmpLineViews)
                tmpLineWidth.add(lineWidth)

                calcWidth = Math.max(calcWidth, lineWidth)
                lineWidth = childWidth
                calcHeight += lineHeight
                lineHeight = Math.max(lineHeight, childHeight)
                tmpLineViews = arrayListOf(child)
            } else {
                lineWidth += childWidth
                lineHeight = Math.max(lineHeight, childHeight)
                tmpLineViews.add(child)
            }

            // 超过最大行数，退出
            if (tmpAllViews.size >= biggestLine - 1) {
                break
            }
        }

        // 最后一行需要再次计算
        if (tmpLineViews.size > 0) {
            calcWidth = Math.max(lineWidth, calcWidth)
            calcHeight += lineHeight
            tmpLineHeight.add(lineHeight)
            tmpAllViews.add(tmpLineViews)
            tmpLineWidth.add(lineWidth)
        }
    }

    private fun getChildMeasureSize(childView: View?): Pair<Int, Int> {
        if (childView == null) return Pair(0, 0)
        val lp = childView.layoutParams as MarginLayoutParams
        val childWidth = childView.measuredWidth + lp.leftMargin + lp.rightMargin
        val childHeight = childView.measuredHeight + lp.topMargin + lp.bottomMargin
        return Pair(childWidth, childHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width: Int = width
        var lineHeight = 0
        var left: Int = paddingLeft
        var top: Int = paddingTop
        val lineNum = mAllViews.size

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
                val rc = lc + child.measuredWidth
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
