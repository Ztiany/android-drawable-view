package com.android.base.ui.drawable.drawable

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.IntDef
import java.lang.ref.WeakReference

/**
 * References：
 *
 * - [又一个减少冗余 Drawable 资源的解决方案](https://mp.weixin.qq.com/s/qxMoI7UTw3WtiRR6oIDGKA)
 * - [CodeGradientDrawable](https://github.com/lizijin/zijiexiaozhan/blob/main/app/src/main/java/com/peter/viewgrouptutorial/drawable/CodeGradientDrawable.kt)
 */
class CodeGradientDrawable private constructor(
    theme: Resources.Theme,
    shapeType: Int,
    gradient: Gradient?,
    corner: Corner?,
    solidColor: ColorStateList?,
    stroke: Stroke?,
    padding: Padding?,
    width: Int,
    height: Int,
) : GradientDrawable() {

    companion object {
        private val sCache = HashMap<Int, WeakReference<CodeGradientDrawable>>()
    }

    init {
        applyTheme(theme)
        shape = shapeType

        solidColor?.let {
            color = it
        }

        gradient?.let {
            with(it) {
                setGradientCenter(this.centerX, this.centerY)
                setUseLevel(this.useLevel)
                setGradientType(this.gradientType)
                setGradientRadius(this.gradientRadius)
                setOrientation(this.orientation)
                colors = this.gradientColors
            }
        }

        corner?.let {
            with(it) {
                if (this.radii == null) {
                    cornerRadius = this.radius
                } else {
                    for (index in this.radii.indices) {
                        if (this.radii[index] == 0.0F) {
                            this.radii[index] = radius
                        }
                    }
                    cornerRadii = this.radii
                }
            }
        }

        stroke?.let {
            with(it) {
                if (this.dashWidth != 0.0F) {
                    setStroke(this.width, this.colorStateList, this.dashWidth, this.dashGap)
                } else {
                    setStroke(this.width, this.colorStateList)
                }
            }
        }

        padding?.let {
            with(it) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    setPadding(this.left, this.top, this.right, this.bottom)
                }
            }
        }

        if (width != -1 && height != -1) {
            setSize(width, height)
        }
    }

    class Builder(context: Context) {

        private var debugName: String? = "Debug"

        private var solidColor: ColorStateList? = null
        private var shape: Int = RECTANGLE

        private var width: Int = -1
        private var height: Int = -1

        private var gradient: Gradient.Builder? = null
        private var corner: Corner.Builder? = null
        private var stroke: Stroke.Builder? = null
        private var padding: Padding.Builder? = null
        private var theme: Resources.Theme = context.theme
        private val metrics = context.resources.displayMetrics

        fun debugName(debugName: String) = apply {
            this.debugName = debugName
        }

        fun shape(shape: Int) = apply {
            this.shape = shape
        }

        fun solidColor(solidColor: ColorStateList) = apply {
            this.solidColor = solidColor
            this.gradient = null
        }

        fun gradient(gradient: Gradient.Builder) = apply {
            this.gradient = gradient
            this.corner = null
        }

        fun corner(corner: Corner.Builder) = apply {
            this.corner = corner
        }

        fun stroke(stroke: Stroke.Builder) = apply {
            this.stroke = stroke
        }

        fun padding(padding: Padding.Builder) = apply {
            this.padding = padding
        }

        @JvmOverloads
        fun size(width: Int, height: Int, @UnixValue unit: Int = DP_UNIT) = apply {
            this.width = getDimensionPixelSize(unit, width.toFloat(), metrics)
            this.height = getDimensionPixelSize(unit, height.toFloat(), metrics)
        }

        /**
         * 当将一个 Drawable 设置为某个 View 的背景时，该 Drawable 的 setBounds 方法将会被调用，然后 Drawable 的绘制区域也会发生变化，
         * 而如果启动缓存机制的话，多个 View 可能共享同一个  Drawable，而如果不同 View 的 Size 不同，或者 View 的 Size 动态变化，都会导致
         * Drawable 的共享出现问题。当你确定 Drawable 的 Size 是固定的时候，可以启动缓存机制（即设置 [cacheable] 为 true），否则不要启动缓存机制。
         */
        fun build(cacheable: Boolean = false): CodeGradientDrawable {
            if (!cacheable) {
                return CodeGradientDrawable(
                    theme, shape, gradient?.build(), corner?.build(), solidColor, stroke?.build(), padding?.build(), width, height
                )
            }

            synchronized(sCache) {
                val key = hashCode()
                val cached = sCache[key]?.get()
                if (cached == null) {
                    println("CodeGradientDrawable is null $this")
                    val drawable = CodeGradientDrawable(
                        theme, shape, gradient?.build(), corner?.build(), solidColor, stroke?.build(), padding?.build(), width, height
                    )
                    sCache[key] = WeakReference(drawable)
                    return drawable
                } else {
                    println("CodeGradientDrawable not null $this")
                    return cached
                }
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Builder

            if (solidColor != other.solidColor) return false
            if (shape != other.shape) return false
            if (width != other.width) return false
            if (height != other.height) return false
            if (gradient != other.gradient) return false
            if (corner != other.corner) return false
            if (stroke != other.stroke) return false
            if (padding != other.padding) return false
            if (debugName != other.debugName) return false

            return true
        }

        override fun hashCode(): Int {
            var result = solidColor?.hashCode() ?: 0
            result = 31 * result + shape
            result = 31 * result + width
            result = 31 * result + height
            result = 31 * result + (gradient?.hashCode() ?: 0)
            result = 31 * result + (corner?.hashCode() ?: 0)
            result = 31 * result + (stroke?.hashCode() ?: 0)
            result = 31 * result + (padding?.hashCode() ?: 0)
            result = 31 * result + (debugName?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "Builder(debugName=$debugName, solidColor=$solidColor, shape=$shape, width=$width, height=$height, gradient=$gradient, corner=$corner, stroke=$stroke, padding=$padding)"
        }

    }// Builder end.

}

class Gradient private constructor(
    internal val centerX: Float,
    internal val centerY: Float,
    internal val useLevel: Boolean,
    internal val gradientType: Int,
    internal val gradientRadius: Float,
    internal val orientation: GradientDrawable.Orientation,
    internal val gradientColors: IntArray,
) {

    class Builder constructor(context: Context) {
        private var centerX: Float = 0.5f
        private var centerY: Float = 0.5f
        private var useLevel: Boolean = false
        private var gradientType: Int = GradientDrawable.LINEAR_GRADIENT
        private var gradientRadius: Float = 0.5f
        private var orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT
        private lateinit var gradientColors: IntArray
        private val metrics = context.resources.displayMetrics

        fun gradientCenter(x: Float, y: Float): Builder = apply {
            this.centerX = x
            this.centerY = y
        }

        fun useLevel(useLevel: Boolean): Builder = apply {
            this.useLevel = useLevel
        }

        fun gradientType(gradientType: Int): Builder = apply {
            this.gradientType = gradientType
        }

        fun orientation(orientation: GradientDrawable.Orientation): Builder = apply {
            this.orientation = orientation
        }

        @JvmOverloads
        fun gradientRadius(gradientRadius: Float, @UnixValue gradientRadiusUnit: Int = DP_UNIT): Builder = apply {
            this.gradientRadius = getDimension(gradientRadiusUnit, gradientRadius, metrics)
        }

        fun gradientColors(colors: IntArray): Builder = apply {
            this.gradientColors = colors
        }

        internal fun build(): Gradient {
            return Gradient(
                centerX, centerY, useLevel, gradientType, gradientRadius, orientation, gradientColors
            )
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Builder

            if (centerX != other.centerX) return false
            if (centerY != other.centerY) return false
            if (useLevel != other.useLevel) return false
            if (gradientType != other.gradientType) return false
            if (gradientRadius != other.gradientRadius) return false
            if (orientation != other.orientation) return false
            if (!gradientColors.contentEquals(other.gradientColors)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = centerX.hashCode()
            result = 31 * result + centerY.hashCode()
            result = 31 * result + useLevel.hashCode()
            result = 31 * result + gradientType
            result = 31 * result + gradientRadius.hashCode()
            result = 31 * result + orientation.hashCode()
            result = 31 * result + gradientColors.contentHashCode()
            return result
        }

        override fun toString(): String {
            return "Gradient.Builder(centerX=$centerX, centerY=$centerY, useLevel=$useLevel, gradientType=$gradientType, gradientRadius=$gradientRadius, orientation=$orientation, gradientColors=${gradientColors.contentToString()})"
        }
    }
}

class Corner private constructor(
    internal val radius: Float, internal val radii: FloatArray? = null,
) {

    class Builder constructor(context: Context) {

        private var radius: Float = 0.0f
        private var radii: FloatArray? = null
        private val metrics = context.resources.displayMetrics

        @JvmOverloads
        fun radius(radius: Float, @UnixValue radiusUnit: Int = DP_UNIT) = apply {
            this.radius = getDimensionPixelSize(radiusUnit, radius, metrics).toFloat()
        }

        @JvmOverloads
        fun radii(
            topLeftRadius: Float = 0f,
            topRightRadius: Float = 0f,
            bottomRightRadius: Float = 0f,
            bottomLeftRadius: Float = 0f,
            @UnixValue radiusUnit: Int = DP_UNIT,
        ) = apply {

            val radii = FloatArray(8)

            val newTopLeftRadius = getDimensionPixelSize(radiusUnit, topLeftRadius, metrics).toFloat()
            radii[0] = newTopLeftRadius
            radii[1] = newTopLeftRadius

            val newTopRightRadius = getDimensionPixelSize(radiusUnit, topRightRadius, metrics).toFloat()
            radii[2] = newTopRightRadius
            radii[3] = newTopRightRadius

            val newBottomRightRadius = getDimensionPixelSize(radiusUnit, bottomRightRadius, metrics).toFloat()

            radii[4] = newBottomRightRadius
            radii[5] = newBottomRightRadius

            val newBottomLeftRadius = getDimensionPixelSize(radiusUnit, bottomLeftRadius, metrics).toFloat()

            radii[6] = newBottomLeftRadius
            radii[7] = newBottomLeftRadius

            this.radii = radii
        }

        internal fun build(): Corner {
            return Corner(radius = radius, radii)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Builder

            if (radius != other.radius) return false
            if (radii != null) {
                if (other.radii == null) return false
                if (!radii.contentEquals(other.radii)) return false
            } else if (other.radii != null) return false

            return true
        }

        override fun hashCode(): Int {
            var result = radius.hashCode()
            result = 31 * result + (radii?.contentHashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "Corner.Builder(radius=$radius, radii=${radii?.contentToString()})"
        }
    }
}

class Stroke private constructor(
    internal val width: Int, internal val colorStateList: CodeColorStateList, internal val dashWidth: Float, internal val dashGap: Float,
) {

    class Builder constructor(context: Context) {
        private var width: Int = 0
        private lateinit var colorStateList: CodeColorStateList
        private var dashWidth: Float = 0.0f
        private var dashGap: Float = 0.0f
        private val metrics = context.resources.displayMetrics

        @JvmOverloads
        fun setStroke(width: Float, @UnixValue widthUnit: Int = DP_UNIT, colorStateList: CodeColorStateList) = apply {
            this.width = getDimensionPixelSize(widthUnit, width, metrics)
            this.colorStateList = colorStateList
        }

        @JvmOverloads
        fun setStroke(
            width: Float, colorStateList: CodeColorStateList, dashWidth: Float, dashGap: Float, @UnixValue unit: Int = DP_UNIT,
        ) = apply {
            this.width = getDimensionPixelSize(unit, width, metrics)
            this.colorStateList = colorStateList
            this.dashWidth = getDimension(unit, dashWidth, metrics)
            this.dashGap = getDimension(unit, dashGap, metrics)
        }

        internal fun build(): Stroke {
            return Stroke(width, colorStateList, dashWidth, dashGap)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Builder

            if (width != other.width) return false
            if (colorStateList != other.colorStateList) return false
            if (dashWidth != other.dashWidth) return false
            if (dashGap != other.dashGap) return false

            return true
        }

        override fun hashCode(): Int {
            var result = width
            result = 31 * result + colorStateList.hashCode()
            result = 31 * result + dashWidth.hashCode()
            result = 31 * result + dashGap.hashCode()
            return result
        }

        override fun toString(): String {
            return "Stroke.Builder(width=$width, colorStateList=$colorStateList, dashWidth=$dashWidth, dashGap=$dashGap)"
        }
    }
}

class Padding private constructor(
    internal val top: Int, internal val bottom: Int, internal val left: Int, internal val right: Int,
) {

    class Builder constructor(context: Context) {
        private var top: Int = 0
        private var bottom: Int = 0
        private var left: Int = 0
        private var right: Int = 0
        private val metrics = context.resources.displayMetrics

        @JvmOverloads
        fun setPadding(
            top: Int = 0,
            bottom: Int = 0,
            left: Int = 0,
            right: Int = 0,
            @UnixValue unit: Int = DP_UNIT,
        ) = apply {
            this.top = getDimensionPixelSize(unit, top.toFloat(), metrics)
            this.bottom = getDimensionPixelSize(unit, bottom.toFloat(), metrics)
            this.left = getDimensionPixelSize(unit, left.toFloat(), metrics)
            this.right = getDimensionPixelSize(unit, right.toFloat(), metrics)
        }

        internal fun build(): Padding {
            return Padding(top, bottom, left, right)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Builder

            if (top != other.top) return false
            if (bottom != other.bottom) return false
            if (left != other.left) return false
            if (right != other.right) return false

            return true
        }

        override fun hashCode(): Int {
            var result = top
            result = 31 * result + bottom
            result = 31 * result + left
            result = 31 * result + right
            return result
        }

        override fun toString(): String {
            return "Padding.Builder(top=$top, bottom=$bottom, left=$left, right=$right)"
        }
    }
}

const val PX_UNIT = 0
const val DP_UNIT = 1

@IntDef(value = [PX_UNIT, DP_UNIT])
@Retention(AnnotationRetention.SOURCE)
annotation class UnixValue

// copy from  #TypedValue.complexToDimensionPixelSize
private fun getDimensionPixelSize(unit: Int, value: Float, metrics: DisplayMetrics): Int {
    val f = TypedValue.applyDimension(
        unit, value, metrics
    )
    val res = (if (f >= 0) f + 0.5f else f - 0.5f).toInt()
    if (res != 0) return res
    if (value == 0F) return 0
    return if (value > 0) 1 else -1
}

private fun getDimension(@UnixValue unit: Int, value: Float, metrics: DisplayMetrics): Float {
    return TypedValue.applyDimension(
        unit, value, metrics
    )
}