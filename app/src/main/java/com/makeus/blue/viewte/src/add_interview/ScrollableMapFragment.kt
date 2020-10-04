package com.makeus.blue.viewte.src.add_interview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.gms.maps.SupportMapFragment

class ScrollableMapFragment : SupportMapFragment() {
    private var listener: OnTouchListener? = null

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        savedInstance: Bundle?
    ): View? {
        val layout = super.onCreateView(layoutInflater, viewGroup, savedInstance)

        val frameLayout = TouchableWrapper(getActivity()!!)

        frameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent))

        (layout as ViewGroup).addView(
            frameLayout,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        return layout
    }

    fun setListener(listener: OnTouchListener) {
        this.listener = listener
    }

    interface OnTouchListener {
        fun onActionDown()
        fun onActionUp()
    }

    inner class TouchableWrapper(context: Context) : FrameLayout(context) {

        override fun dispatchTouchEvent(event: MotionEvent): Boolean {
            if (listener == null) {
                return super.dispatchTouchEvent(event)
            }

            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> listener!!.onActionDown()
                MotionEvent.ACTION_UP -> listener!!.onActionUp()
            }
            return super.dispatchTouchEvent(event)
        }
    }
}