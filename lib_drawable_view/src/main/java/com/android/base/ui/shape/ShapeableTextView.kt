package com.android.base.ui.shape

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatTextView
import com.android.base.ui.common.TextColor
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.textview.MaterialTextView

/** Please refer [MaterialShapeDrawableHelper] for details. */
class ShapeableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr), EnhancedShapeable, TextColor {

    private val mdHelper = MaterialShapeDrawableHelper(context, attrs, defStyleAttr)

    private val colorHelper = ShapeTextColorHelper(context, attrs, defStyleAttr)

    init {
        mdHelper.update(this)
        colorHelper.setTextColor(this)
    }

    override fun updateShapeDrawable() {
        mdHelper.update(this)
    }

    override fun updateTextColor() {
        colorHelper.setTextColor(this)
    }

    override fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        mdHelper.updateShapeAppearanceModel(shapeAppearanceModel)
    }

    override fun getShapeAppearanceModel(): ShapeAppearanceModel {
        return mdHelper.obtainShapeAppearanceModel()
    }

}