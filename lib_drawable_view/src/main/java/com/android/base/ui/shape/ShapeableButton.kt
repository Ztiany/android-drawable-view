package com.android.base.ui.shape

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatButton
import com.android.base.ui.common.RecoverableTextColor
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

/** Please refer [MaterialShapeDrawableHelper] for details. */
class ShapeableButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.buttonStyle
) : AppCompatButton(context, attrs, defStyleAttr), EnhancedShapeable, RecoverableTextColor {

    private val mdHelper = MaterialShapeDrawableHelper(context, attrs, defStyleAttr)

    private val colorHelper = ShapeTextColorHelper(context, attrs, defStyleAttr)

    init {
        mdHelper.setShapeDrawable(this)
        colorHelper.setTextColor(this)
    }

    override fun recoverShapeDrawable() {
        mdHelper.recoverShapeDrawable(this)
    }

    override fun recoverTextColor() {
        colorHelper.setTextColor(this)
    }

    override fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        mdHelper.updateShapeAppearanceModel(shapeAppearanceModel)
    }

    override fun getShapeAppearanceModel(): ShapeAppearanceModel {
        return mdHelper.obtainShapeAppearanceModel()
    }

    override fun getShapeDrawable(): MaterialShapeDrawable {
        return mdHelper.drawable
    }

}