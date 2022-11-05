package com.com.android.base.ui.shape

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.textview.MaterialTextView

/** Please refer [MaterialShapeDrawableHelper] for details. */
class ShapeableEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr), EnhancedShapeable, EnhancedTextView {

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