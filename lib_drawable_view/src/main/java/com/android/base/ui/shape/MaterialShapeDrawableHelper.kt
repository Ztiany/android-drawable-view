package com.android.base.ui.shape

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.View
import com.android.base.ui.drawable.parser.ResourceInfo
import com.android.base.ui.drawable.parser.StateChecked
import com.android.base.ui.drawable.parser.StateEnabled
import com.android.base.ui.drawable.parser.StateFocused
import com.android.base.ui.drawable.parser.StatePressed
import com.android.base.ui.drawable.parser.StateSelected
import com.android.base.ui.drawable.parser.parseCodeColorStateListAttribute
import com.android.base.ui.drawables.R
import com.android.base.ui.utils.getColorStateListSafely
import com.google.android.material.resources.MaterialResources
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

/**
 * The Shapeable function in material only supports following components:
 *
 *  1. Chip
 *  2. MaterialCard
 *  3. MaterialButton
 *  4. ShapeableImageView
 *  5. FloatingActionButton
 *
 * But the MaterialShapeDrawable is pretty functional, we can use it to support more.
 *
 * references:
 *
 *  1. [Material Components——Shape 的处理](https://juejin.cn/post/7096291397447188517)
 *  2. [Material Components——ShapeableImageView](https://juejin.cn/post/7111500924358492173)
 *  3. [To create rounded corners for a view without having to create a separate drawable](https://stackoverflow.com/questions/59046711/android-is-there-a-simple-way-to-create-rounded-corners-for-a-view-without-havi)
 *
 * notes: the background attribute of views that uses MaterialShapeDrawableHelper may not work.
 *@author Ztiany
 */
class MaterialShapeDrawableHelper(
    private val context: Context,
    attrs: AttributeSet?,
    defaultStyleAttr: Int = 0,
    defaultStyleRes: Int = 0,
) {

    private var shapeAppearanceModel: ShapeAppearanceModel

    var shapeDrawable: MaterialShapeDrawable
        private set

    var rippleDrawable: RippleDrawable? = null
        private set

    init {
        val mdTypedValue = context.obtainStyledAttributes(attrs, R.styleable.MaterialShapeDrawableView, defaultStyleAttr, defaultStyleRes)
        shapeAppearanceModel = ShapeAppearanceModel.builder(context, attrs, defaultStyleAttr, defaultStyleRes).build()
        shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            setDrawableStyle(mdTypedValue)
        }
        mdTypedValue.recycle()

        val rippleTypedValue = context.obtainStyledAttributes(attrs, R.styleable.ShapeableView, defaultStyleAttr, defaultStyleRes)

        if (rippleTypedValue.hasValue(R.styleable.ShapeableView_msd_ripple_color)) {
            val rippleColor = rippleTypedValue.getColorStateListSafely("msd_ripple_color", R.styleable.ShapeableView_msd_ripple_color)
            /*
                <enum name="content" value="1" />
                <enum name="mask" value="2" />
                <enum name="none" value="3" />
             */
            when (rippleTypedValue.getInt(R.styleable.ShapeableView_msd_ripple_type, 1)) {
                1 -> rippleDrawable = RippleDrawable(rippleColor, shapeDrawable, null)
                2 -> rippleDrawable = RippleDrawable(rippleColor, null, shapeDrawable)
//                3 -> rippleDrawable = RippleDrawable(rippleColor, null, null)
                3 -> rippleDrawable =RippleDrawable(ColorStateList.valueOf(Color.RED), null, null)
            }
        }

        rippleTypedValue.recycle()
    }

    @SuppressLint("RestrictedApi")
    private fun MaterialShapeDrawable.setDrawableStyle(typedValue: TypedArray) {
        fillColor = if (typedValue.hasValue(R.styleable.MaterialShapeDrawableView_msd_backgroundColor)) {
            MaterialResources.getColorStateList(context, typedValue, R.styleable.MaterialShapeDrawableView_msd_backgroundColor)
        } else {
            createFillColorListCustomAttr(typedValue)
        }

        if (typedValue.hasValue(R.styleable.MaterialShapeDrawableView_msd_strokeWidth)) {
            strokeWidth = typedValue.getDimension(R.styleable.MaterialShapeDrawableView_msd_strokeWidth, 0F)
            strokeColor = if (typedValue.hasValue(R.styleable.MaterialShapeDrawableView_msd_strokeColor)) {
                MaterialResources.getColorStateList(context, typedValue, R.styleable.MaterialShapeDrawableView_msd_strokeColor)
            } else {
                createStrokeListByCustomAttr(typedValue)
            }
        }
    }

    private fun createFillColorListCustomAttr(typedArray: TypedArray): ColorStateList {
        return parseCodeColorStateListAttribute(typedArray, buildBackgroundResourceList()) ?: ColorStateList.valueOf(Color.TRANSPARENT)
    }

    private fun buildBackgroundResourceList() = listOf(
        ResourceInfo(R.styleable.MaterialShapeDrawableView_msd_backgroundColor_disabled, StateEnabled, false),
        ResourceInfo(R.styleable.MaterialShapeDrawableView_msd_backgroundColor_pressed, StatePressed, true),
        ResourceInfo(R.styleable.MaterialShapeDrawableView_msd_backgroundColor_checked, StateChecked, true),
        ResourceInfo(R.styleable.MaterialShapeDrawableView_msd_backgroundColor_selected, StateSelected, true),
        ResourceInfo(R.styleable.MaterialShapeDrawableView_msd_backgroundColor_focused, StateFocused, true),
        ResourceInfo(R.styleable.MaterialShapeDrawableView_msd_backgroundColor_normal, null, false)
    )

    private fun createStrokeListByCustomAttr(typedArray: TypedArray): ColorStateList {
        return parseCodeColorStateListAttribute(typedArray, buildStrokeResourceList()) ?: ColorStateList.valueOf(Color.TRANSPARENT)
    }

    private fun buildStrokeResourceList() = listOf(
        ResourceInfo(R.styleable.MaterialShapeDrawableView_msd_strokeColor_disabled, StateEnabled, false),
        ResourceInfo(R.styleable.MaterialShapeDrawableView_msd_strokeColor_pressed, StatePressed, true),
        ResourceInfo(R.styleable.MaterialShapeDrawableView_msd_strokeColor_checked, StateChecked, true),
        ResourceInfo(R.styleable.MaterialShapeDrawableView_msd_strokeColor_selected, StateSelected, true),
        ResourceInfo(R.styleable.MaterialShapeDrawableView_msd_strokeColor_focused, StateFocused, true),
        ResourceInfo(R.styleable.MaterialShapeDrawableView_msd_strokeColor_normal, null, false)
    )

    fun setShapeDrawable(target: View) {
        val left = target.paddingLeft
        val top = target.paddingTop
        val right = target.paddingRight
        val bottom = target.paddingBottom

        rippleDrawable?.let {
            target.background = it
        } ?: run {
            target.background = shapeDrawable
        }

        target.setPadding(left, top, right, bottom)
    }

    fun recoverShapeDrawable(target: View) {
        shapeDrawable.shapeAppearanceModel = shapeAppearanceModel
        setShapeDrawable(target)
    }

    fun updateShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        shapeDrawable.shapeAppearanceModel = shapeAppearanceModel
        rippleDrawable?.invalidateSelf()
    }

    fun obtainShapeAppearanceModel(): ShapeAppearanceModel {
        return shapeDrawable.shapeAppearanceModel
    }

}