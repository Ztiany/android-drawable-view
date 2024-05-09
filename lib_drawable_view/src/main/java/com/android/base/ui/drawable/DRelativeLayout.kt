package com.android.base.ui.drawable

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes

class DRelativeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes), RecoverableDrawableView {

    private val codeDrawableHelper = CodeDrawableHelper(context, attrs, defStyleAttr, defStyleRes)

    init {
        codeDrawableHelper.setBackground(this)
    }

    override fun recoverDrawable() {
        codeDrawableHelper.setBackground(this)
    }

}
