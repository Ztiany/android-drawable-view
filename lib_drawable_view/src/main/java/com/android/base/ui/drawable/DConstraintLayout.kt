package com.android.base.ui.drawable

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout

open class DConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes), RecoverableDrawableView {

    private val codeDrawableHelper = CodeDrawableHelper(context, attrs, defStyleAttr, defStyleRes)

    init {
        codeDrawableHelper.setBackground(this)
        CodeViewStateHelper.setSelectedState(attrs, defStyleAttr, 0, this)
    }

    override fun recoverDrawable() {
        codeDrawableHelper.setBackground(this)
    }

}
