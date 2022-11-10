package com.android.base.ui.drawable

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import com.android.base.ui.common.TextColorView
import com.google.android.material.textview.MaterialTextView

class DTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.textViewStyle
) : MaterialTextView(context, attrs, defStyleAttr), DrawableView, TextColorView {

    private val codeDrawableHelper = CodeDrawableHelper(context, attrs, defStyleAttr)

    private val codeTextColorStateListHelper = CodeTextColorStateListHelper(context, attrs, defStyleAttr)

    init {
        codeDrawableHelper.setBackground(this)
        codeTextColorStateListHelper.setTextColor(this)
    }

    override fun updateDrawable() {
        codeDrawableHelper.setBackground(this)
    }

    override fun updateTextColor() {
        codeTextColorStateListHelper.setTextColor(this)
    }

}
