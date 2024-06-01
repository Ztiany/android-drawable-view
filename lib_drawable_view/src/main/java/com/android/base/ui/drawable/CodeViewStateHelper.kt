package com.android.base.ui.drawable

import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.use
import com.ztiany.android.drawable.view.R

object CodeViewStateHelper {

    fun setSelectedState(
        attrs: AttributeSet?,
        defaultStyleAttr: Int = 0,
        defaultStyleRes: Int = 0,
        view: View
    ) {
        view.context.obtainStyledAttributes(attrs, R.styleable.DView, defaultStyleAttr, defaultStyleRes).use {
            if (it.hasValue(R.styleable.DView_cdv_selected)) {
                view.isSelected = it.getBoolean(R.styleable.DView_cdv_selected, false)
            }
        }
    }

}
