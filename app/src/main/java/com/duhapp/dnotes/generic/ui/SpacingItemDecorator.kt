package com.duhapp.dnotes.generic.ui

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
    val topSpace: Int,
    val bottomSpace: Int,
    val leftSpace: Int,
    val rightSpace: Int
)