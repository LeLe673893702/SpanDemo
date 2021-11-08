@file: JvmName("ViewExtentions")

package com.example.spandemo

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.EditText
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd



/**
 * 高度变化动画
 */
fun View.heightAnimator(startHeight: Int, endHeight: Int,
                        finishCall: (() -> Unit)? = null
): ValueAnimator {
    val heightAnimator = ValueAnimator.ofInt(startHeight, endHeight)
    heightAnimator.addUpdateListener {
        layoutParams.height = it.animatedValue as Int
        requestLayout()
    }
    heightAnimator.doOnEnd { finishCall?.invoke() }
    heightAnimator.doOnCancel { finishCall?.invoke() }
    heightAnimator.interpolator = AccelerateInterpolator()
    heightAnimator.duration = 200
    heightAnimator.start()
    return heightAnimator
}

/**
 * 高度变化动画
 */
fun View.heightAnimator(startHeight: Int, endHeight: Int,
                        duration: Long = 200L,
                        updateListener: (() -> Unit)? = null,
                        finishCall: (() -> Unit)? = null
) {
    val heightAnimator = ValueAnimator.ofInt(startHeight, endHeight)
    heightAnimator.addUpdateListener {
        layoutParams.height = it.animatedValue as Int
        requestLayout()
        if (updateListener != null) {
            updateListener()
        }
    }
    heightAnimator.doOnEnd { finishCall?.invoke() }
    heightAnimator.doOnCancel { finishCall?.invoke() }
    heightAnimator.interpolator = AccelerateInterpolator()
    heightAnimator.duration = duration
    heightAnimator.start()
}

fun View.hideAnimate() {
    val scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 1f, 0f)
    val scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 1f, 0f)
    val alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
    val animatorSet = AnimatorSet()
    animatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator)
    animatorSet.duration = 200
    animatorSet.interpolator = AccelerateInterpolator()
    animatorSet.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {}

        override fun onAnimationEnd(animation: Animator?) {
            visibility = View.GONE
        }

        override fun onAnimationCancel(animation: Animator?) {}

        override fun onAnimationRepeat(animation: Animator?) {}
    })
    animatorSet.start()
}

fun View.showAnimate() {
    visibility = View.VISIBLE
    val scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 0f, 1f)
    val scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 0f, 1f)
    val alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
    val animatorSet = AnimatorSet()
    animatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator)
    animatorSet.duration = 200
    animatorSet.interpolator = AccelerateInterpolator()
    animatorSet.start()
}


fun View.translationYAnimate(translationY: Float) {
    val translationYAnimator = ObjectAnimator.ofFloat(this, "translationY", translationY)
    translationYAnimator.duration = 208
    translationYAnimator.interpolator = AccelerateInterpolator()
    translationYAnimator.start()
}

//旋转动画
fun View.rotateAnimate(angle: Float, duration: Long = 350) {
    val rotateAnimator = ObjectAnimator.ofFloat(this, "rotation", rotation + angle)
    rotateAnimator.duration = duration
    rotateAnimator.interpolator = AccelerateInterpolator()
    rotateAnimator.start()
}

inline fun View.onFocusChange(crossinline hasFocus: (Boolean) -> Unit) {
    onFocusChangeListener = View.OnFocusChangeListener { _, b -> }
}