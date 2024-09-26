package com.android.base.ui.drawable.parser

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.view.ContextThemeWrapper
import com.android.base.ui.drawables.R
import com.android.base.ui.utils.getColorStateListSafely
import timber.log.Timber

/** refer [R.styleable.CodeRippleDrawable] */
internal fun parseRippleDrawableAttribute(context: Context, typedArray: TypedArray): Drawable? {
    return internalParseRippleDrawableAttribute(context, typedArray)
}

internal fun parseRippleDrawableAttributeByStyle(context: Context, resourceId: Int): Drawable? {
    val contextThemeWrapper = ContextThemeWrapper(context, resourceId)
    val gradientTypedValue = contextThemeWrapper.obtainStyledAttributes(R.styleable.CodeRippleDrawable)
    val drawable = parseRippleDrawableAttribute(contextThemeWrapper, gradientTypedValue)
    gradientTypedValue.recycle()
    return drawable
}

private fun internalParseRippleDrawableAttribute(context: Context, typedArray: TypedArray): Drawable? {
    val colorStateList = parseRippleColorList(typedArray) ?: return null
    val contentDrawable = parseDrawableByStyleOrDrawable(context, typedArray, R.styleable.CodeRippleDrawable_crd_ripple_content_drawable)
    val maskDrawable = parseDrawableByStyleOrDrawable(context, typedArray, R.styleable.CodeRippleDrawable_crd_ripple_mask_drawable)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        RippleDrawable(colorStateList, contentDrawable, maskDrawable)
    } else {
        Timber.w("RippleDrawable is not supported before Android 5.0")
        return null
    }
}

private fun parseRippleColorList(typedArray: TypedArray): ColorStateList? {
    return if (typedArray.hasValue(R.styleable.CodeRippleDrawable_crd_ripple_color)) {
        typedArray.getColorStateListSafely("crd_ripple_color", R.styleable.CodeRippleDrawable_crd_ripple_color)
    } else null
}

private fun parseDrawableByStyleOrDrawable(context: Context, typedArray: TypedArray, drawableResourceId: Int): Drawable? {
    if (!typedArray.hasValue(drawableResourceId)) {
        return null
    }
    val resourceId = typedArray.getResourceId(drawableResourceId, -1)
    val typeName = context.resources.getResourceTypeName(resourceId)
    Timber.d("parseDrawableByStyleOrDrawable: resourceId=$resourceId, typeName=$typeName")
    if (typeName == "drawable" || typeName == "mipmap" || typeName == "color") {
        return typedArray.getDrawable(drawableResourceId).apply {
            Timber.d("parseDrawableByStyleOrDrawable: drawable=$this")
        }
    }

    if (typeName == "style") {
        //尝试按照 Gradient 解析
        val gradientTypedArray = ContextThemeWrapper(context, resourceId).obtainStyledAttributes(R.styleable.CodeGradientDrawable)
        var drawable = parseGradientDrawableAttribute(context, gradientTypedArray)
        gradientTypedArray.recycle()

        //尝试按照 Selector 解析
        if (drawable == null) {
            val selectorTypedArray = ContextThemeWrapper(context, resourceId).obtainStyledAttributes(R.styleable.CodeSelectorDrawable)
            drawable = parseSelectorDrawableAttribute(context, gradientTypedArray)
            selectorTypedArray.recycle()
        }
        return drawable
    }

    return null
}