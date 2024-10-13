/*
 *  Copyright (c) 2024 deysak <deysakos@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.deysak.nestedscrollingwebview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebView
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.ScrollAxis

/**
 * NestedScrollingWebView is-a [WebView] that implements [NestedScrollingChild].
 *
 * It is useful for purposes where [WebView] is required to be:
 *  1. Scrollable
 *  2. Zoomable
 *  3. Supporting nested scrolling
 *
 * **Example:** Hiding the toolbar upon scroll
 * ([Issue-14991](https://github.com/ankidroid/Anki-Android/issues/14991)).
 *
 * **Need:**
 * There is no default way to reliably support all the 3 aforementioned behaviors as:
 *  * Using [androidx.core.widget.NestedScrollView] interferes with the zoom
 *  ([Issue-16135](https://github.com/ankidroid/Anki-Android/issues/16135)).
 * * Intercepting the scale motion events from [androidx.core.widget.NestedScrollView]
 * and passing it to [WebView] is unreliable.
 * * Hiding the toolbar by detecting scroll is unreliable.
 * * Workarounds for ensuring reliable zoom behavior interferes with scrolling.
 * * Methods allowing consistent scroll and zoom behavior are not reliable for hiding toolbar.
 *
 * **Usage:**
 * 1. [WebView.setNestedScrollingEnabled] must be set to `true`.
 * 2. XML: `<com.deysak.nestedscrollingwebview.NestedScrollingWebView ... />`
 */
class NestedScrollingWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.webViewStyle
) : WebView(context, attrs, defStyleAttr), NestedScrollingChild {
    private val yAxis: Int = 1

    private val deltaX: Int = 0

    private val mChildHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this)

    private val mScrollOffset: IntArray = IntArray(2)

    private val mScrollConsumed: IntArray = IntArray(2)

    private var mLastMotionY: Int = 0

    private var mNestedYOffset: Int = 0

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        val actionMasked: Int = motionEvent.actionMasked
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0
        }

        val velocityTrackerMotionEvent: MotionEvent = MotionEvent.obtain(motionEvent)
        handleOffset(velocityTrackerMotionEvent, mNestedYOffset)

        val motionEventY: Int = motionEvent.y.toInt()
        when (actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mLastMotionY = motionEventY
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
            }

            MotionEvent.ACTION_MOVE -> {
                val scrollDistanceY: Int = mLastMotionY - motionEventY
                if (dispatchNestedPreScroll(
                        deltaX,
                        scrollDistanceY,
                        mScrollConsumed,
                        mScrollOffset
                    )
                ) {
                    handleOffset(velocityTrackerMotionEvent, mScrollOffset[yAxis])
                    mNestedYOffset += mScrollOffset[yAxis]
                }
                mLastMotionY = motionEventY - mScrollOffset[yAxis]
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                stopNestedScroll()
            }
        }

        velocityTrackerMotionEvent.recycle()
        return super.onTouchEvent(velocityTrackerMotionEvent)
    }

    private fun handleOffset(velocityTrackerMotionEvent: MotionEvent, deltaY: Int) {
        velocityTrackerMotionEvent.offsetLocation(deltaX.toFloat(), deltaY.toFloat())
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper.setNestedScrollingEnabled(enabled)
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mChildHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(@ScrollAxis axes: Int): Boolean {
        return mChildHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        mChildHelper.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mChildHelper.hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?
    ): Boolean {
        return mChildHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow
        )
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }
}
