package com.android.base.ui.shape

import com.google.android.material.shape.Shapeable

interface RecoverableShapeable : Shapeable {

    fun recoverShapeDrawable()

}