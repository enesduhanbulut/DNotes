package com.duhapp.dnotes.features.generic.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecorator(
    private val spaceModel: SpaceModel
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = spaceModel.topSpace
        outRect.bottom = spaceModel.bottomSpace
        outRect.left = spaceModel.leftSpace
        outRect.right = spaceModel.rightSpace
    }
}

data class SpaceModel(
    val topSpace: Int = 0,
    val bottomSpace: Int = 0,
    val leftSpace: Int = 0,
    val rightSpace: Int = 0
) {
    constructor(space: Int) : this(space, space, space, space)

    constructor(
        verticalSpace: Int = 0,
        horizontalSpace: Int = 0
    ) : this(verticalSpace, verticalSpace, horizontalSpace, horizontalSpace)
}