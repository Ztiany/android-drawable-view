package com.android.base.ui.shape

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

/** Please refer [MaterialShapeDrawableHelper] for details. */
open class ShapeableConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes), EnhancedShapeable {

    private val mdHelper = MaterialShapeDrawableHelper(context, attrs, defStyleAttr, defStyleRes)

    init {
        mdHelper.setShapeDrawable(this)
        ShapeViewStateHelper.setSelectedState(attrs, defStyleAttr, 0, this)
    }

    override fun recoverShapeDrawable() {
        mdHelper.recoverShapeDrawable(this)
    }

    override fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        mdHelper.updateShapeAppearanceModel(shapeAppearanceModel)
    }

    override fun getShapeAppearanceModel(): ShapeAppearanceModel {
        return mdHelper.obtainShapeAppearanceModel()
    }

    override fun getShapeDrawable(): MaterialShapeDrawable {
        return mdHelper.shapeDrawable
    }

}