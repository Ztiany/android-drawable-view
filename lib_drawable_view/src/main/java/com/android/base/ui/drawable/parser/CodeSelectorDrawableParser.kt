package com.android.base.ui.drawable.parser

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.view.ContextThemeWrapper
import com.android.base.ui.drawable.drawable.CodeStateListDrawable
import com.android.base.ui.drawable.drawable.SelectorDrawableItem
import com.ztiany.android.drawable.view.R

internal fun parseSelectorDrawableAttributeByStyle(context: Context, resourceId: Int): Drawable? {
    val contextThemeWrapper = ContextThemeWrapper(context, resourceId)
    val gradientTypedValue = contextThemeWrapper.obtainStyledAttributes(R.styleable.CodeSelectorDrawable)
    val drawable = parseSelectorDrawableAttribute(contextThemeWrapper, gradientTypedValue)
    gradientTypedValue.recycle()
    return drawable
}

/** refer [R.styleable.CodeSelectorDrawable] */
internal fun parseSelectorDrawableAttribute(context: Context, typedArray: TypedArray): Drawable? {
    val drawableList = mutableListOf<StateInfo<Drawable>>()

    val collectStateDrawable = fun(drawableResourceId: Int, state: State?, add: Boolean) {
        parseDrawableByStyleOrDrawable(context, typedArray, drawableResourceId)?.let {
            drawableList.add(StateInfo(it, state, add = add))
        }
    }

    collectStateDrawable(R.styleable.CodeSelectorDrawable_csd_selector_state_disabled, StateEnabled, false)
    collectStateDrawable(R.styleable.CodeSelectorDrawable_csd_selector_state_focused, StateFocused, true)
    collectStateDrawable(R.styleable.CodeSelectorDrawable_csd_selector_state_checked, StateChecked, true)
    collectStateDrawable(R.styleable.CodeSelectorDrawable_csd_selector_state_selected, StateSelected, true)
    collectStateDrawable(R.styleable.CodeSelectorDrawable_csd_selector_state_pressed, StatePressed, true)
    collectStateDrawable(R.styleable.CodeSelectorDrawable_csd_selector_state_normal, null, false)

    if (drawableList.isEmpty()) {
        return null
    }

    return CodeStateListDrawable.Builder().apply {
        drawableList.forEach {
            addSelectorDrawableItem(SelectorDrawableItem.Builder().apply {
                drawable(it.value)
                if (it.state != null) {
                    if (it.add) {
                        addState(it.state)
                    } else {
                        minusState(it.state)
                    }
                }
            })
        }
    }.build()
}

private fun parseDrawableByStyleOrDrawable(context: Context, typedArray: TypedArray, drawableResourceId: Int): Drawable? {
    if (!typedArray.hasValue(drawableResourceId)) {
        return null
    }
    val resourceId = typedArray.getResourceId(drawableResourceId, -1)
    val typeName = context.resources.getResourceTypeName(resourceId)
    if (typeName == "drawable") {
        return typedArray.getDrawable(drawableResourceId)
    } else if (typeName == "style") {
        val gradientTypedArray = ContextThemeWrapper(context, resourceId).obtainStyledAttributes(R.styleable.CodeGradientDrawable)
        val drawable = parseGradientDrawableAttribute(context, gradientTypedArray)
        gradientTypedArray.recycle()
        return drawable
    }
    return null
}