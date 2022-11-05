package com.peter.viewgrouptutorial.drawable

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes

class ELinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes), DrawableView {

    private val codeDrawableHelper = CodeDrawableHelper(context, attrs, defStyleAttr, defStyleRes)

    init {
        codeDrawableHelper.setBackground(this)
    }

    override fun updateDrawable() {
        codeDrawableHelper.setBackground(this)
    }

}
