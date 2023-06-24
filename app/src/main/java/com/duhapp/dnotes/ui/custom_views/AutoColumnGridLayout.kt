package com.duhapp.dnotes.ui.custom_views

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.GridLayoutManager


class AutoColumnGridLayout(
    private val elementGrowth: Int,
    private val context: Context?,
    spanCount: Int,
    orientation: Int,
    reverseLayout: Boolean,
) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {
    override fun onLayoutChildren(
        recycler: androidx.recyclerview.widget.RecyclerView.Recycler?,
        state: androidx.recyclerview.widget.RecyclerView.State?
    ) {
        super.onLayoutChildren(recycler, state)
        if (state!!.isPreLayout) {
            return
        }
        val displayMetrics: DisplayMetrics = context?.resources?.displayMetrics ?: return
        val density = displayMetrics.density
        val dpWidth = width / density
        val dpHeight = height / density
        val dpPaddingRight = paddingRight / density
        val dpPaddingLeft = paddingLeft / density

        val totalSpace: Float = if (orientation == androidx.recyclerview.widget.LinearLayoutManager.VERTICAL) {
            dpWidth - dpPaddingRight - dpPaddingLeft
        } else {
            dpHeight - dpPaddingRight - dpPaddingLeft
        }
        val totalSpan = totalSpace / (elementGrowth / density)
        val spanCount = 1.0f.coerceAtLeast(totalSpan).toInt()
        setSpanCount(spanCount)
    }
}