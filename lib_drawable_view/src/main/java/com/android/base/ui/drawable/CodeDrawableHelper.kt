package com.android.base.ui.drawable

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.use
import com.android.base.ui.drawable.parser.parseGradientDrawableAttribute
import com.android.base.ui.drawable.parser.parseGradientDrawableAttributeByStyle
import com.android.base.ui.drawable.parser.parseRippleDrawableAttribute
import com.android.base.ui.drawable.parser.parseRippleDrawableAttributeByStyle
import com.android.base.ui.drawable.parser.parseSelectorDrawableAttribute
import com.android.base.ui.drawable.parser.parseSelectorDrawableAttributeByStyle
import com.ztiany.android.drawable.view.R

class CodeDrawableHelper(
    private val context: Context,
    private val attrs: AttributeSet?,
    private val defaultStyleAttr: Int = 0,
    private val defaultStyleRes: Int = 0
) {

    private var drawable: Drawable? = null

    init {
        withStyleable(R.styleable.CodeDrawableView) {
            buildDrawableByAttributes(this)
        }
    }

    fun setBackground(view: View) {
        drawable?.let {
            val left = view.paddingLeft
            val top = view.paddingTop
            val right = view.paddingRight
            val bottom = view.paddingBottom
            view.background = it
            view.setPadding(left, top, right, bottom)
        }
    }

    private fun withStyleable(styleId: IntArray, action: TypedArray.() -> Unit) {
        context.obtainStyledAttributes(attrs, styleId, defaultStyleAttr, defaultStyleRes).use {
            it.action()
        }
    }

    private fun buildDrawableByAttributes(codingDrawableView: TypedArray) {
        if (codingDrawableView.hasValue(R.styleable.CodeDrawableView_cdv_drawable_type)) {
            when (codingDrawableView.getInt(R.styleable.CodeDrawableView_cdv_drawable_type, -1)) {
                1/*gradient*/ -> buildGradientDrawable()
                2/*selector*/ -> buildSelectorDrawable()
                3/*ripple*/ -> buildRippleDrawable()
                else -> throw IllegalArgumentException("Unsupported drawable type")
            }
            return
        }

        buildDrawableByAppearance(codingDrawableView)
    }

    private fun buildDrawableByAppearance(codingDrawableView: TypedArray) {
        var resourceId = codingDrawableView.getResourceId(R.styleable.CodeDrawableView_cdv_gradient_appearance, -1)
        if (resourceId != -1) {
            drawable = parseGradientDrawableAttributeByStyle(context, resourceId)
            return
        }

        resourceId = codingDrawableView.getResourceId(R.styleable.CodeDrawableView_cdv_selector_appearance, -1)
        if (resourceId != -1) {
            drawable = parseSelectorDrawableAttributeByStyle(context, resourceId)
            return
        }

        resourceId = codingDrawableView.getResourceId(R.styleable.CodeDrawableView_cdv_ripple_appearance, -1)
        if (resourceId != -1) {
            drawable = parseRippleDrawableAttributeByStyle(context, resourceId)
            return
        }
    }

    private fun buildSelectorDrawable() {
        withStyleable(R.styleable.CodeSelectorDrawable) {
            drawable = parseSelectorDrawableAttribute(context, this)
        }
    }

    private fun buildGradientDrawable() {
        withStyleable(R.styleable.CodeGradientDrawable) {
            drawable = parseGradientDrawableAttribute(context, this)
        }
    }

    private fun buildRippleDrawable() {
        withStyleable(R.styleable.CodeRippleDrawable) {
            drawable = parseRippleDrawableAttribute(context, this)
        }
    }

}