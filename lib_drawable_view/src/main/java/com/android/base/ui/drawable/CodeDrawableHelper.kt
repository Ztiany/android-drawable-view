package com.android.base.ui.drawable

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.use
import com.android.base.ui.drawable.parser.parseGradientDrawableAttribute
import com.android.base.ui.drawable.parser.parseGradientDrawableAttributeByStyle
import com.android.base.ui.drawable.parser.parseRippleDrawableAttribute
import com.android.base.ui.drawable.parser.parseRippleDrawableAttributeByStyle
import com.android.base.ui.drawable.parser.parseSelectorDrawableAttribute
import com.android.base.ui.drawable.parser.parseSelectorDrawableAttributeByStyle
import com.android.base.ui.drawables.R

class CodeDrawableHelper(
    private val context: Context,
    private val attrs: AttributeSet?,
    private val defaultStyleAttr: Int = 0,
    private val defaultStyleRes: Int = 0,
) {

    private var backgroundDrawable: Drawable? = null

    private var foregroundDrawable: Drawable? = null

    init {
        withStyleable(R.styleable.CodeDrawableView) {
            buildDrawableByAttributes(this)
        }
    }

    fun setDrawable(view: View) {
        setBackground(view)
        setForeground(view)
    }

    fun setBackground(view: View) {
        backgroundDrawable?.let {
            val left = view.paddingLeft
            val top = view.paddingTop
            val right = view.paddingRight
            val bottom = view.paddingBottom
            view.background = it
            view.setPadding(left, top, right, bottom)
        }
    }

    fun setForeground(view: View) {
        if (view is FrameLayout) {
            foregroundDrawable?.let {
                val left = view.paddingLeft
                val top = view.paddingTop
                val right = view.paddingRight
                val bottom = view.paddingBottom
                view.foreground = it
                view.setPadding(left, top, right, bottom)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            foregroundDrawable?.let {
                val left = view.paddingLeft
                val top = view.paddingTop
                val right = view.paddingRight
                val bottom = view.paddingBottom
                view.foreground = it
                view.setPadding(left, top, right, bottom)
            }
        }
    }

    private fun withStyleable(styleId: IntArray, action: TypedArray.() -> Unit) {
        context.obtainStyledAttributes(attrs, styleId, defaultStyleAttr, defaultStyleRes).use {
            it.action()
        }
    }

    private fun buildDrawableByAttributes(typedArray: TypedArray) {
        if (typedArray.hasValue(R.styleable.CodeDrawableView_cdv_drawable_type)) {
            when (typedArray.getInt(R.styleable.CodeDrawableView_cdv_drawable_type, -1)) {
                1/*gradient*/ -> buildGradientDrawable()
                2/*selector*/ -> buildSelectorDrawable()
                3/*ripple*/ -> buildRippleDrawable()
                else -> throw IllegalArgumentException("Unsupported drawable type")
            }
        } else {
            buildDrawableByAppearance(typedArray)
        }

        buildForegroundDrawableByAppearance(typedArray)
    }

    private fun buildSelectorDrawable() {
        withStyleable(R.styleable.CodeSelectorDrawable) {
            backgroundDrawable = parseSelectorDrawableAttribute(context, this)
        }
    }

    private fun buildGradientDrawable() {
        withStyleable(R.styleable.CodeGradientDrawable) {
            backgroundDrawable = parseGradientDrawableAttribute(context, this)
        }
    }

    private fun buildRippleDrawable() {
        withStyleable(R.styleable.CodeRippleDrawable) {
            backgroundDrawable = parseRippleDrawableAttribute(context, this)
        }
    }

    private fun buildDrawableByAppearance(typedArray: TypedArray) {
        var resourceId = typedArray.getResourceId(R.styleable.CodeDrawableView_cdv_gradient_appearance, -1)
        if (resourceId != -1) {
            backgroundDrawable = parseGradientDrawableAttributeByStyle(context, resourceId)
            return
        }

        resourceId = typedArray.getResourceId(R.styleable.CodeDrawableView_cdv_selector_appearance, -1)
        if (resourceId != -1) {
            backgroundDrawable = parseSelectorDrawableAttributeByStyle(context, resourceId)
            return
        }

        resourceId = typedArray.getResourceId(R.styleable.CodeDrawableView_cdv_ripple_appearance, -1)
        if (resourceId != -1) {
            backgroundDrawable = parseRippleDrawableAttributeByStyle(context, resourceId)
            return
        }
    }

    private fun buildForegroundDrawableByAppearance(typedArray: TypedArray) {
        var resourceId = typedArray.getResourceId(R.styleable.CodeDrawableView_cdv_foreground_gradient_appearance, -1)
        if (resourceId != -1) {
            foregroundDrawable = parseGradientDrawableAttributeByStyle(context, resourceId)
            return
        }

        resourceId = typedArray.getResourceId(R.styleable.CodeDrawableView_cdv_foreground_selector_appearance, -1)
        if (resourceId != -1) {
            foregroundDrawable = parseSelectorDrawableAttributeByStyle(context, resourceId)
            return
        }

        resourceId = typedArray.getResourceId(R.styleable.CodeDrawableView_cdv_foreground_ripple_appearance, -1)
        if (resourceId != -1) {
            foregroundDrawable = parseRippleDrawableAttributeByStyle(context, resourceId)
            return
        }
    }

}