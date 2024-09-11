package com.android.base.ui.utils

import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.StyleableRes
import timber.log.Timber

internal fun TypedArray.getColorSafely(
    name: String,
    @StyleableRes styleable: Int,
    @ColorInt defaultColor: Int = Color.TRANSPARENT,
): Int {
    try {
        return getColor(styleable, defaultColor)
    } catch (e: Exception) {
        Timber.e(e, "getColorSafely.getColor for $name")
    }
    Timber.w("getColorSafely for $name failed, return defaultColor")
    return defaultColor
}

internal fun TypedArray.getColorStateListSafely(
    name: String,
    @StyleableRes styleable: Int,
    defaultColorStateList: ColorStateList = ColorStateList.valueOf(Color.TRANSPARENT),
): ColorStateList {
    try {
        return getColorStateList(styleable) ?: defaultColorStateList
    } catch (e: Exception) {
        Timber.e(e, "getColorSafely.getColor for $name")
    }
    Timber.w("getColorStateListSafely for $name failed, return defaultColor")
    return defaultColorStateList
}