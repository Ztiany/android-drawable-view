package com.android.base.ui.drawable

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatTextView
import com.android.base.ui.common.RecoverableTextColor

open class DTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.textViewStyle,
) : AppCompatTextView(context, attrs, defStyleAttr), RecoverableDrawableView, RecoverableTextColor {

    private val codeDrawableHelper = CodeDrawableHelper(context, attrs, defStyleAttr)

    private val codeTextColorStateListHelper = CodeTextColorStateListHelper(context, attrs, defStyleAttr)

    init {
        codeDrawableHelper.setDrawable(this)
        codeTextColorStateListHelper.setTextColor(this)
        CodeViewStateHelper.setSelectedState(attrs, defStyleAttr, 0, this)
    }

    override fun recoverDrawable() {
        codeDrawableHelper.setDrawable(this)
    }

    override fun recoverTextColor() {
        codeTextColorStateListHelper.setTextColor(this)
    }

}
