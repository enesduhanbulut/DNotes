package com.duhapp.dnotes.features.base.ui

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.addSwipeListener(
    mIcon: Drawable,
    mBackground: ColorDrawable,
    onMove: (position: Int) -> Unit,
    onSwiped: (position: Int) -> Unit,
    swipeDirection: SwipeDirection
) {
    val direction =
        if (swipeDirection == SwipeDirection.LEFT) ItemTouchHelper.LEFT else ItemTouchHelper.RIGHT
    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, direction) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            onMove.invoke(viewHolder.absoluteAdapterPosition)
            return false
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            val itemView = viewHolder.itemView
            val backgroundCornerOffset =
                25 //so mBackground is behind the rounded corners of itemView
            val iconMargin: Int = (itemView.height - mIcon.intrinsicHeight) / 2
            val iconTop: Int = itemView.top + (itemView.height - mIcon.intrinsicHeight) / 2
            val iconBottom: Int = iconTop + mIcon.intrinsicHeight
            if (dX > 0) { // Swiping to the right
                val iconLeft = itemView.left + iconMargin
                val iconRight: Int = iconLeft + mIcon.intrinsicWidth
                mIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                mBackground.setBounds(
                    itemView.left, itemView.top,
                    itemView.left + dX.toInt() + backgroundCornerOffset, itemView.bottom
                )
            } else if (dX < 0) { // Swiping to the left
                val iconLeft: Int = itemView.right - iconMargin - mIcon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                mIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                mBackground.setBounds(
                    itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom
                )
            } else { // view is unSwiped
                mIcon.setBounds(0, 0, 0, 0)
                mBackground.setBounds(0, 0, 0, 0)
            }
            mBackground.draw(c)
            mIcon.draw(c)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            onSwiped.invoke(viewHolder.absoluteAdapterPosition)
        }
    }).attachToRecyclerView(this)

}
enum class SwipeDirection {
    LEFT, RIGHT
}