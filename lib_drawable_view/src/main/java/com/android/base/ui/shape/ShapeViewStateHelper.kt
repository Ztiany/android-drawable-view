package com.android.base.ui.shape

import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.use
import com.android.base.ui.drawables.R

object ShapeViewStateHelper {

    fun setSelectedState(
        attrs: AttributeSet?,
        defaultStyleAttr: Int = 0,
        defaultStyleRes: Int = 0,
        view: View,
    ) {
        view.context.obtainStyledAttributes(
            attrs,
            R.styleable.ShapeableView,
            defaultStyleAttr,
            defaultStyleRes
        ).use {
            if (it.hasValue(R.styleable.ShapeableView_msd_selected)) {
                view.isSelected = it.getBoolean(R.styleable.ShapeableView_msd_selected, false)
            }
            view.clipToOutline = it.getBoolean(R.styleable.ShapeableView_msd_clip_to_outline, true)
        }
    }

}
