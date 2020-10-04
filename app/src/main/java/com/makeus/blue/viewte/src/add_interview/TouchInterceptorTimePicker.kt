package com.makeus.blue.viewte.src.add_interview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TimePicker

class TouchInterceptorTimePicker : TimePicker {
    constructor(context: Context) : super(context) {}

    //This is the important constructor
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            //Excluding the top of the view
            if (ev.y < height / 3.3f)
                return false

            val p = parent
            p?.requestDisallowInterceptTouchEvent(true)
        }

        return false
    }
}