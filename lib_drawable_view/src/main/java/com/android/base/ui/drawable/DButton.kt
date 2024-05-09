package com.android.base.ui.drawable

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatButton
import com.android.base.ui.common.RecoverableTextColor

class DButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr), RecoverableDrawableView, RecoverableTextColor {

    private val codeDrawableHelper = CodeDrawableHelper(context, attrs, defStyleAttr)

    private val codeTextColorStateListHelper = CodeTextColorStateListHelper(context, attrs, defStyleAttr)

    init {
        codeDrawableHelper.setBackground(this)
        codeTextColorStateListHelper.setTextColor(this)
    }

    override fun recoverDrawable() {
        codeDrawableHelper.setBackground(this)
    }

    override fun recoverTextColor() {
        codeTextColorStateListHelper.setTextColor(this)
    }

}
