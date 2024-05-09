package com.android.base.ui.drawable.parser

internal class StateInfo<out T>(
    val value: T,
    val state: State?,
    val add: Boolean
)

internal class ResourceInfo(
    val resourceId: Int,
    val state: State?,
    val add: Boolean
)