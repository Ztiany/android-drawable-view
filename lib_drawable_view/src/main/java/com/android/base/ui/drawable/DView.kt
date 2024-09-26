package com.android.base.ui.drawable

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes

open class DView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes), RecoverableDrawableView {

    private val codeDrawableHelper = CodeDrawableHelper(context, attrs, defStyleAttr, defStyleRes)

    init {
        codeDrawableHelper.setDrawable(this)
        CodeViewStateHelper.setSelectedState(attrs, defStyleAttr, 0, this)
    }

    override fun recoverDrawable() {
        codeDrawableHelper.setDrawable(this)
    }

}