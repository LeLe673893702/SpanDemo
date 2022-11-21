package com.example.spandemo.nest

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup


class ExpandTouchDelegateHelper(
    private val mParentView: ViewGroup,
    private val mTargetView: View,
    private val mLeftExpand: Int,
    private val mRightExpand: Int,
    private val mTopExpand: Int,
    private val mBottomExpand: Int
) {
    fun expandTouchDelegate() {
        val currentTargetView = mTargetView
        val bounds = Rect()
        TapViewGroupUtils.getDescendantRect(mParentView, mTargetView, bounds)
        Log.d("ExpandMultiTouchDelegate", "ExpandTouchDelegateHelper---${bounds.toShortString()}")
        bounds.left -= mLeftExpand
        bounds.top -= mTopExpand
        bounds.right += mRightExpand
        bounds.bottom += mBottomExpand
        val touchDelegate = ExpandMultiTouchDelegate(
            bounds,
            currentTargetView,
            mParentView,
            mLeftExpand,
            mRightExpand,
            mTopExpand,
            mBottomExpand
        )
        mParentView.touchDelegate = touchDelegate
    }
}