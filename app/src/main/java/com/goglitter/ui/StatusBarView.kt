package com.goglitter.ui

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.WindowInsets


class StatusBarView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var mStatusBarHeight = 0

    constructor(context: Context?) : this(context, null) {}

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBarHeight = insets.systemWindowInsetTop
            return insets.consumeSystemWindowInsets()
        }
        return insets
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mStatusBarHeight)
    }
}