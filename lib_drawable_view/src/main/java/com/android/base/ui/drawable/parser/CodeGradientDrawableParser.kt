package com.android.base.ui.drawable.parser

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.ContextThemeWrapper
import androidx.core.content.res.use
import com.android.base.ui.drawable.drawable.CodeColorStateList
import com.android.base.ui.drawable.drawable.CodeGradientDrawable
import com.android.base.ui.drawable.drawable.Corner
import com.android.base.ui.drawable.drawable.Gradient
import com.android.base.ui.drawable.drawable.PX_UNIT
import com.android.base.ui.drawable.drawable.Padding
import com.android.base.ui.drawable.drawable.Stroke
import com.android.base.ui.drawables.R

/** refer [R.styleable.CodeGradientDrawable] */
internal fun parseGradientDrawableAttribute(context: Context, typedArray: TypedArray): Drawable? {
    return internalParseGradientDrawableAttribute(context, typedArray)
}

internal fun parseGradientDrawableAttributeByStyle(context: Context, resourceId: Int): Drawable? {
    val contextThemeWrapper = ContextThemeWrapper(context, resourceId)
    val gradientTypedValue = contextThemeWrapper.obtainStyledAttributes(R.styleable.CodeGradientDrawable)
    val drawable = internalParseGradientDrawableAttribute(contextThemeWrapper, gradientTypedValue)
    gradientTypedValue.recycle()
    return drawable
}

private fun internalParseGradientDrawableAttribute(
    context: Context, typedArray: TypedArray
): CodeGradientDrawable? {
    val shapeValue = typedArray.getInt(R.styleable.CodeGradientDrawable_cgd_shape, -1)
    if (shapeValue == -1) {
        return null
    }

    val size = parseSizeAttribute(context, typedArray)
    val cornerBuilder = parseCornerAttribute(context, typedArray)
    val gradientBuilder = parseGradientAttribute(context, typedArray)
    val strokeBuilder = parseStrokeAttribute(context, typedArray)
    val paddingBuilder = parsePaddingAttribute(context, typedArray)

    return CodeGradientDrawable.Builder(context).apply {
        shape(shapeValue)

        if (typedArray.hasValue(R.styleable.CodeGradientDrawable_cgd_shape_solid)) {
            solidColor(CodeColorStateList.valueOf(typedArray.getColor(R.styleable.CodeGradientDrawable_cgd_shape_solid, Color.WHITE)))
        }

        size.let { size(it[0], it[1], PX_UNIT) }
        gradientBuilder?.let { gradient(it) }
        corner(cornerBuilder)
        strokeBuilder?.let { stroke(it) }
        paddingBuilder?.let { padding(it) }
    }.build()
}

private class GradientValues {
    var centerX: Float = 0.5F
    var centerY: Float = 0.5F
    var useLevel: Boolean = false
    var type: Int = GradientDrawable.LINEAR_GRADIENT
    var radius: Float = 0.5F
    var orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT
    var gradientColors: IntArray? = null
}

private fun parseGradientAttribute(context: Context, typedArray: TypedArray): Gradient.Builder? {
    val gradientValues = GradientValues()
    val gradientResourceId = typedArray.getResourceId(R.styleable.CodeGradientDrawable_cgd_shape_gradient_style, -1)
    if (gradientResourceId != -1) {
        ContextThemeWrapper(context, gradientResourceId).obtainStyledAttributes(R.styleable.GradientStyle).use {
            getGradientAttributeValue(it, gradientValues)
        }
    }

    getGradientAttributeValue2(typedArray, gradientValues)

    val gradientColors = gradientValues.gradientColors
    if (gradientColors == null || gradientColors.isEmpty()) {
        return null
    }

    return Gradient.Builder(context).apply {
        gradientType(gradientValues.type)
        useLevel(gradientValues.useLevel)
        orientation(gradientValues.orientation)
        gradientColors(gradientColors)
        gradientCenter(gradientValues.centerX, gradientValues.centerY)
        gradientRadius(gradientValues.radius, PX_UNIT)
    }
}

private fun getGradientAttributeValue(gradientTypedValue: TypedArray, gradientValues: GradientValues) {
    val colorList = mutableListOf<Int>()
    if (gradientTypedValue.hasValue(R.styleable.GradientStyle_gradient_style_start_color)) {
        colorList.add(gradientTypedValue.getColor(R.styleable.GradientStyle_gradient_style_start_color, Color.WHITE))
    }
    if (gradientTypedValue.hasValue(R.styleable.GradientStyle_gradient_style_center_color)) {
        colorList.add(gradientTypedValue.getColor(R.styleable.GradientStyle_gradient_style_center_color, Color.WHITE))
    }
    if (gradientTypedValue.hasValue(R.styleable.GradientStyle_gradient_style_end_color)) {
        colorList.add(gradientTypedValue.getColor(R.styleable.GradientStyle_gradient_style_end_color, Color.WHITE))
    }
    if (colorList.isNotEmpty()) {
        gradientValues.gradientColors = colorList.toIntArray()
    } else {
        return
    }

    if (gradientTypedValue.hasValue(R.styleable.GradientStyle_gradient_style_orientation)) {
        val orientation = gradientTypedValue.getColor(
            R.styleable.GradientStyle_gradient_style_orientation, GradientDrawable.Orientation.LEFT_RIGHT.ordinal
        )
        gradientValues.orientation = GradientDrawable.Orientation.entries.toTypedArray()[orientation]
    }

    if (gradientTypedValue.hasValue(R.styleable.GradientStyle_gradient_style_center_x)) {
        gradientValues.centerX = gradientTypedValue.getFloat(R.styleable.GradientStyle_gradient_style_center_x, 0.5F)
    }
    if (gradientTypedValue.hasValue(R.styleable.GradientStyle_gradient_style_center_y)) {
        gradientValues.centerY = gradientTypedValue.getFloat(R.styleable.GradientStyle_gradient_style_center_y, 0.5F)
    }
    if (gradientTypedValue.hasValue(R.styleable.GradientStyle_gradient_style_radius)) {
        gradientValues.radius = gradientTypedValue.getDimension(R.styleable.GradientStyle_gradient_style_radius, 0.5F)
    }
    if (gradientTypedValue.hasValue(R.styleable.GradientStyle_gradient_style_radius)) {
        gradientValues.useLevel = gradientTypedValue.getBoolean(R.styleable.GradientStyle_gradient_style_radius, false)
    }
    if (gradientTypedValue.hasValue(R.styleable.GradientStyle_gradient_style_type)) {
        gradientValues.type =
            gradientTypedValue.getInt(R.styleable.GradientStyle_gradient_style_type, GradientDrawable.LINEAR_GRADIENT)
    }
}

private fun getGradientAttributeValue2(gradientTypedValue: TypedArray, gradientValues: GradientValues) {
    val colorList = mutableListOf<Int>()
    if (gradientTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_gradient_start_color)) {
        colorList.add(gradientTypedValue.getColor(R.styleable.CodeGradientDrawable_cgd_gradient_start_color, Color.WHITE))
    }
    if (gradientTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_gradient_center_color)) {
        colorList.add(gradientTypedValue.getColor(R.styleable.CodeGradientDrawable_cgd_gradient_center_color, Color.WHITE))
    }
    if (gradientTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_gradient_end_color)) {
        colorList.add(gradientTypedValue.getColor(R.styleable.CodeGradientDrawable_cgd_gradient_end_color, Color.WHITE))
    }
    if (colorList.isNotEmpty()) {
        gradientValues.gradientColors = colorList.toIntArray()
    } else {
        return
    }

    if (gradientTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_gradient_orientation)) {
        val orientation = gradientTypedValue.getColor(
            R.styleable.CodeGradientDrawable_cgd_gradient_orientation, GradientDrawable.Orientation.LEFT_RIGHT.ordinal
        )
        gradientValues.orientation = GradientDrawable.Orientation.entries.toTypedArray()[orientation]
    }

    if (gradientTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_gradient_center_x)) {
        gradientValues.centerX = gradientTypedValue.getFloat(R.styleable.CodeGradientDrawable_cgd_gradient_center_x, 0.5F)
    }
    if (gradientTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_gradient_center_y)) {
        gradientValues.centerY = gradientTypedValue.getFloat(R.styleable.CodeGradientDrawable_cgd_gradient_center_y, 0.5F)
    }
    if (gradientTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_gradient_radius)) {
        gradientValues.radius = gradientTypedValue.getDimension(R.styleable.CodeGradientDrawable_cgd_gradient_radius, 0.5F)
    }
    if (gradientTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_gradient_radius)) {
        gradientValues.useLevel = gradientTypedValue.getBoolean(R.styleable.CodeGradientDrawable_cgd_gradient_radius, false)
    }
    if (gradientTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_gradient_type)) {
        gradientValues.type = gradientTypedValue.getInt(R.styleable.CodeGradientDrawable_cgd_gradient_type, GradientDrawable.LINEAR_GRADIENT)
    }
}

private class StrokeValues {
    var dashWidth: Float = 0.5F
    var dashGap: Float = 0.5F
    var width: Float = 0.5F
    var color: Int = 0
}

private fun parseStrokeAttribute(context: Context, typedArray: TypedArray): Stroke.Builder? {
    val strokeValues = StrokeValues()
    val strokeResourceId = typedArray.getResourceId(R.styleable.CodeGradientDrawable_cgd_shape_stroke_style, -1)
    if (strokeResourceId != -1) {
        ContextThemeWrapper(context, strokeResourceId).obtainStyledAttributes(R.styleable.StrokeStyle).use {
            getStrokeAttributeValue(it, strokeValues)
        }
    }

    getStrokeAttributeValue2(typedArray, strokeValues)

    if (strokeValues.width == 0F) {
        return null
    }

    return Stroke.Builder(context).apply {
        setStroke(strokeValues.width, CodeColorStateList.valueOf(strokeValues.color), strokeValues.dashWidth, strokeValues.dashGap, unit = PX_UNIT)
    }
}

private fun getStrokeAttributeValue(strokeTypedValue: TypedArray, strokeValues: StrokeValues) {
    if (strokeTypedValue.hasValue(R.styleable.StrokeStyle_stroke_style_dash_width)) {
        strokeValues.dashWidth = strokeTypedValue.getDimension(R.styleable.StrokeStyle_stroke_style_dash_width, 0F)
    }
    if (strokeTypedValue.hasValue(R.styleable.StrokeStyle_stroke_style_dash_gap)) {
        strokeValues.dashGap = strokeTypedValue.getDimension(R.styleable.StrokeStyle_stroke_style_dash_gap, 0F)
    }
    if (strokeTypedValue.hasValue(R.styleable.StrokeStyle_stroke_style_width)) {
        strokeValues.width = strokeTypedValue.getDimension(R.styleable.StrokeStyle_stroke_style_width, 0F)
    }
    if (strokeTypedValue.hasValue(R.styleable.StrokeStyle_stroke_style_color)) {
        strokeValues.color = strokeTypedValue.getColor(R.styleable.StrokeStyle_stroke_style_color, Color.TRANSPARENT)
    }
}

private fun getStrokeAttributeValue2(strokeTypedValue: TypedArray, strokeValues: StrokeValues) {
    if (strokeTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_stroke_dash_width)) {
        strokeValues.dashWidth = strokeTypedValue.getDimension(R.styleable.CodeGradientDrawable_cgd_stroke_dash_width, 0F)
    }
    if (strokeTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_stroke_dash_gap)) {
        strokeValues.dashGap = strokeTypedValue.getDimension(R.styleable.CodeGradientDrawable_cgd_stroke_dash_gap, 0F)
    }
    if (strokeTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_stroke_width)) {
        strokeValues.width = strokeTypedValue.getDimension(R.styleable.CodeGradientDrawable_cgd_stroke_width, 0F)
    }
    if (strokeTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_stroke_color)) {
        strokeValues.color = strokeTypedValue.getColor(R.styleable.CodeGradientDrawable_cgd_stroke_color, Color.TRANSPARENT)
    }
}

private fun parsePaddingAttribute(context: Context, typedArray: TypedArray): Padding.Builder? {
    val paddings = Rect()
    val paddingResourceId = typedArray.getResourceId(R.styleable.CodeGradientDrawable_cgd_shape_padding_style, -1)
    if (paddingResourceId != -1) {
        ContextThemeWrapper(context, paddingResourceId).obtainStyledAttributes(R.styleable.PaddingStyle).use {
            getPaddingAttributeValue(it, paddings)
        }
    }

    getPaddingAttributeValue2(typedArray, paddings)

    return if (paddings.isEmpty) {
        null
    } else {
        Padding.Builder(context).apply {
            setPadding(paddings.top, paddings.bottom, paddings.left, paddings.right, unit = PX_UNIT)
        }
    }
}

private fun getPaddingAttributeValue(paddingTypedValue: TypedArray, paddings: Rect) {
    if (paddingTypedValue.hasValue(R.styleable.PaddingStyle_padding_style_left)) {
        paddings.left = paddingTypedValue.getDimensionPixelOffset(R.styleable.PaddingStyle_padding_style_left, 0)
    }
    if (paddingTypedValue.hasValue(R.styleable.PaddingStyle_padding_style_right)) {
        paddings.right = paddingTypedValue.getDimensionPixelOffset(R.styleable.PaddingStyle_padding_style_right, 0)
    }
    if (paddingTypedValue.hasValue(R.styleable.PaddingStyle_padding_style_top)) {
        paddings.top = paddingTypedValue.getDimensionPixelOffset(R.styleable.PaddingStyle_padding_style_top, 0)
    }
    if (paddingTypedValue.hasValue(R.styleable.PaddingStyle_padding_style_bottom)) {
        paddings.bottom = paddingTypedValue.getDimensionPixelOffset(R.styleable.PaddingStyle_padding_style_bottom, 0)
    }
}

private fun getPaddingAttributeValue2(paddingTypedValue: TypedArray, paddings: Rect) {
    if (paddingTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_padding_left)) {
        paddings.left = paddingTypedValue.getDimensionPixelOffset(R.styleable.CodeGradientDrawable_cgd_padding_left, 0)
    }
    if (paddingTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_padding_right)) {
        paddings.right = paddingTypedValue.getDimensionPixelOffset(R.styleable.CodeGradientDrawable_cgd_padding_right, 0)
    }
    if (paddingTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_padding_top)) {
        paddings.top = paddingTypedValue.getDimensionPixelOffset(R.styleable.CodeGradientDrawable_cgd_padding_top, 0)
    }
    if (paddingTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_padding_bottom)) {
        paddings.bottom = paddingTypedValue.getDimensionPixelOffset(R.styleable.CodeGradientDrawable_cgd_padding_bottom, 0)
    }
}

private fun parseSizeAttribute(context: Context, typedArray: TypedArray): IntArray {
    val defaultSize = intArrayOf(-1, -1)

    val sizeResourceId = typedArray.getResourceId(R.styleable.CodeGradientDrawable_cgd_shape_size_style, -1)
    if (sizeResourceId != -1) {
        ContextThemeWrapper(context, sizeResourceId).obtainStyledAttributes(R.styleable.SizeStyle).use {
            getSizeAttributeValue(it, defaultSize)
        }
    }

    getSizeAttributeValue2(typedArray, defaultSize)

    return defaultSize
}

private fun getSizeAttributeValue(sizeTypedValue: TypedArray, size: IntArray) {
    if (sizeTypedValue.hasValue(R.styleable.SizeStyle_size_style_width)) {
        size[0] = sizeTypedValue.getDimensionPixelSize(R.styleable.SizeStyle_size_style_width, -1)
    }
    if (sizeTypedValue.hasValue(R.styleable.SizeStyle_size_style_height)) {
        size[1] = sizeTypedValue.getDimensionPixelSize(R.styleable.SizeStyle_size_style_height, -1)
    }
}

private fun getSizeAttributeValue2(sizeTypedValue: TypedArray, size: IntArray) {
    if (sizeTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_width)) {
        size[0] = sizeTypedValue.getDimensionPixelSize(R.styleable.CodeGradientDrawable_cgd_width, -1)
    }
    if (sizeTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_height)) {
        size[1] = sizeTypedValue.getDimensionPixelSize(R.styleable.CodeGradientDrawable_cgd_height, -1)
    }
}

private fun parseCornerAttribute(context: Context, typedArray: TypedArray): Corner.Builder {
    val corners = floatArrayOf(0F, 0F, 0F, 0F)

    val cornerResourceId = typedArray.getResourceId(R.styleable.CodeGradientDrawable_cgd_shape_corner_style, -1)
    if (cornerResourceId != -1) {
        ContextThemeWrapper(context, cornerResourceId).obtainStyledAttributes(R.styleable.CornerStyle).use {
            getCornerAttributeValues(it, corners)
        }
    }

    getCornerAttributeValues2(typedArray, corners)

    return Corner.Builder(context).apply {
        radii(
            topLeftRadius = corners[0],
            topRightRadius = corners[1],
            bottomLeftRadius = corners[2],
            bottomRightRadius = corners[3],
            radiusUnit = PX_UNIT
        )
    }
}

private fun getCornerAttributeValues(cornerTypedValue: TypedArray, cornerValue: FloatArray) {
    if (cornerTypedValue.hasValue(R.styleable.CornerStyle_corner_style_all)) {
        val corner = cornerTypedValue.getDimension(R.styleable.CornerStyle_corner_style_all, 0F)
        cornerValue[0] = corner
        cornerValue[1] = corner
        cornerValue[2] = corner
        cornerValue[3] = corner
    }

    if (cornerTypedValue.hasValue(R.styleable.CornerStyle_corner_style_top_left)) {
        cornerValue[0] = cornerTypedValue.getDimension(R.styleable.CornerStyle_corner_style_top_left, cornerValue[0])
    }
    if (cornerTypedValue.hasValue(R.styleable.CornerStyle_corner_style_top_right)) {
        cornerValue[1] = cornerTypedValue.getDimension(R.styleable.CornerStyle_corner_style_top_right, cornerValue[1])
    }
    if (cornerTypedValue.hasValue(R.styleable.CornerStyle_corner_style_bottom_left)) {
        cornerValue[2] = cornerTypedValue.getDimension(R.styleable.CornerStyle_corner_style_bottom_left, cornerValue[2])
    }
    if (cornerTypedValue.hasValue(R.styleable.CornerStyle_corner_style_bottom_right)) {
        cornerValue[3] = cornerTypedValue.getDimension(R.styleable.CornerStyle_corner_style_bottom_right, cornerValue[3])
    }
}

private fun getCornerAttributeValues2(cornerTypedValue: TypedArray, cornerValue: FloatArray) {
    if (cornerTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_corner)) {
        val corner = cornerTypedValue.getDimension(R.styleable.CodeGradientDrawable_cgd_corner, 0F)
        cornerValue[0] = corner
        cornerValue[1] = corner
        cornerValue[2] = corner
        cornerValue[3] = corner
    }

    if (cornerTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_corner_top_left)) {
        cornerValue[0] = cornerTypedValue.getDimension(R.styleable.CodeGradientDrawable_cgd_corner_top_left, cornerValue[0])
    }
    if (cornerTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_corner_top_right)) {
        cornerValue[1] = cornerTypedValue.getDimension(R.styleable.CodeGradientDrawable_cgd_corner_top_right, cornerValue[1])
    }
    if (cornerTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_corner_bottom_left)) {
        cornerValue[2] = cornerTypedValue.getDimension(R.styleable.CodeGradientDrawable_cgd_corner_bottom_left, cornerValue[2])
    }
    if (cornerTypedValue.hasValue(R.styleable.CodeGradientDrawable_cgd_corner_bottom_right)) {
        cornerValue[3] = cornerTypedValue.getDimension(R.styleable.CodeGradientDrawable_cgd_corner_bottom_right, cornerValue[3])
    }
}
