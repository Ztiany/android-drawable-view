package com.android.base.ui.drawable.parser

import android.content.res.TypedArray
import android.graphics.Color
import com.android.base.ui.drawable.drawable.CodeColorStateList
import com.android.base.ui.drawable.drawable.SelectorColorItem

internal fun parseCodeColorStateListAttribute(typedArray: TypedArray, targets: List<ResourceInfo>): CodeColorStateList? {
    val colorList = mutableListOf<StateInfo<Int>>()

    targets.forEach {
        addColor(colorList, typedArray, it)
    }

    if (colorList.isEmpty()) {
        return null
    }

    return CodeColorStateList.Builder().apply {
        colorList.forEach {
            addSelectorColorItem(SelectorColorItem.Builder().apply {
                if (it.state != null) {
                    if (it.add) {
                        addState(it.state)
                    } else {
                        minusState(it.state)
                    }
                }
                color(it.value)
            })
        }
    }.build()
}

private fun addColor(list: MutableList<StateInfo<Int>>, typedArray: TypedArray, resourceInfo: ResourceInfo) {
    if (typedArray.hasValue(resourceInfo.resourceId)) {
        val color = typedArray.getColor(resourceInfo.resourceId, Color.WHITE)
        list.add(StateInfo(color, resourceInfo.state, resourceInfo.add))
    }
}
