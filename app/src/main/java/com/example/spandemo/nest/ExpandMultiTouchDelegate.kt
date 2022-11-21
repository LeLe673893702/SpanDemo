package com.example.spandemo.nest

import android.graphics.Rect
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup


class ExpandMultiTouchDelegate(
    private val mBounds: Rect,
    delegateView: View,
    parentView: ViewGroup,
    leftExpand: Int,
    rightExpand: Int,
    topExpand: Int,
    bottomExpand: Int
) : TouchDelegate(mBounds, delegateView) {
    private var mDelegateTargeted = false
    private val mSlopBounds: Rect
    private val mChildInParentBounds = Rect()
    private val mDelegateView: View
    private val mSlop: Int
    private val mParentView: ViewGroup
    private val mLeftExpand: Int
    private val mRightExpand: Int
    private val mTopExpand: Int
    private val mBottomExpand: Int

    init {
        mSlop = ViewConfiguration.get(delegateView.context).scaledTouchSlop
        mSlopBounds = Rect(mBounds)
        mSlopBounds.inset(-mSlop, -mSlop)
        mDelegateView = delegateView
        mParentView = parentView
        mLeftExpand = leftExpand
        mRightExpand = rightExpand
        mBottomExpand = bottomExpand
        mTopExpand = topExpand
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        var sendToDelegate = false
        var hit = true
        var handled = false
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mDelegateTargeted = mBounds.contains(x, y)

                sendToDelegate = mDelegateTargeted
            }
            MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_UP, MotionEvent.ACTION_MOVE -> {
                sendToDelegate = mDelegateTargeted
                if (sendToDelegate) {
                    val slopBounds = mSlopBounds
                    if (!slopBounds.contains(x, y)) {
                        hit = false
                    }
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                sendToDelegate = mDelegateTargeted
                mDelegateTargeted = false
            }
        }
        if (sendToDelegate) {
            val delegateView = mDelegateView
            if (hit) {
                //主要修改在这，针对加减购布局，加购和减购按钮特殊处理
                if (delegateView is ViewGroup) {
                    val count = delegateView.childCount
                    for (i in 0 until count) {
                        if (i != 0 && i != count - 1) continue
                        val child = delegateView.getChildAt(i)
                        TapViewGroupUtils.getDescendantRect(
                            mParentView,
                            child,
                            mChildInParentBounds
                        )
                        mChildInParentBounds.top -= mTopExpand
                        mChildInParentBounds.bottom += mBottomExpand
                        mChildInParentBounds.left -= mLeftExpand
                        mChildInParentBounds.right += mRightExpand
                        if (mChildInParentBounds.contains(x, y)) {
                            event.setLocation(
                                (child.width / 2).toFloat(),
                                (child.height / 2).toFloat()
                            )
                            return child.dispatchTouchEvent(event)
                        }
                    }
                    return false
                } else {
                    event.setLocation(
                        (delegateView.width / 2).toFloat(),
                        (delegateView.height / 2).toFloat()
                    )
                }
            } else {
                val slop = mSlop
                event.setLocation(-(slop * 2).toFloat(), -(slop * 2).toFloat())
            }
            handled = delegateView.dispatchTouchEvent(event)
        }
        return handled
    }
}