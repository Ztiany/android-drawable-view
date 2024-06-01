package com.android.base.ui.drawable

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes

open class DLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes), RecoverableDrawableView {

    private val codeDrawableHelper = CodeDrawableHelper(context, attrs, defStyleAttr, defStyleRes)

    init {
        codeDrawableHelper.setBackground(this)
        CodeViewStateHelper.setSelectedState(attrs, defStyleAttr, 0, this)
    }

    override fun recoverDrawable() {
        codeDrawableHelper.setBackground(this)
    }

}
