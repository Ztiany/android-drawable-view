package com.android.base.ui.shape

import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.Shapeable

interface EnhancedShapeable : Shapeable {

    fun recoverShapeDrawable()

    fun getShapeDrawable(): MaterialShapeDrawable

}