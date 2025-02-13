package com.android.base.ui.drawable

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.res.use
import com.android.base.ui.drawable.drawable.CodeColorStateList
import com.android.base.ui.drawable.parser.ResourceInfo
import com.android.base.ui.drawable.parser.StateChecked
import com.android.base.ui.drawable.parser.StateEnabled
import com.android.base.ui.drawable.parser.StateFocused
import com.android.base.ui.drawable.parser.StatePressed
import com.android.base.ui.drawable.parser.StateSelected
import com.android.base.ui.drawable.parser.parseCodeColorStateListAttribute
import com.android.base.ui.drawables.R

class CodeTextColorStateListHelper(
    private val context: Context,
    private val attrs: AttributeSet?,
    private val defaultStyleAttr: Int = 0,
    private val defaultStyleRes: Int = 0
) {

    private var colorStateList: CodeColorStateList? = null

    init {
        withStyleable(R.styleable.DTextView) {
            colorStateList = parseCodeColorStateListAttribute(this, buildResourceList())
        }
    }

    private fun buildResourceList() = listOf(
        ResourceInfo(R.styleable.DTextView_dtv_text_color_disabled, StateEnabled, false),
        ResourceInfo(R.styleable.DTextView_dtv_text_color_pressed, StatePressed, true),
        ResourceInfo(R.styleable.DTextView_dtv_text_color_checked, StateChecked, true),
        ResourceInfo(R.styleable.DTextView_dtv_text_color_selected, StateSelected, true),
        ResourceInfo(R.styleable.DTextView_dtv_text_color_focused, StateFocused, true),
        ResourceInfo(R.styleable.DTextView_dtv_text_color_normal, null, false)
    )

    private fun withStyleable(styleId: IntArray, action: TypedArray.() -> Unit) {
        context.obtainStyledAttributes(attrs, styleId, defaultStyleAttr, defaultStyleRes).use {
            it.action()
        }
    }

    fun setTextColor(textView: TextView) {
        colorStateList?.let {
            textView.setTextColor(it)
        }
    }

}