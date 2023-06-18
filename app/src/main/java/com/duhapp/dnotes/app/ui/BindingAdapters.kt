package com.duhapp.dnotes.app.ui

import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.base.ui.BaseListAdapter
import com.duhapp.dnotes.features.base.ui.BaseListItem
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView


@BindingAdapter("setUnderlined")
fun setUnderlined(materialTextView: MaterialTextView, isUnderlined: Boolean) {
    if (isUnderlined) {
        materialTextView.paintFlags =
            materialTextView.paintFlags or android.graphics.Paint.UNDERLINE_TEXT_FLAG
    } else {
        materialTextView.paintFlags =
            materialTextView.paintFlags and android.graphics.Paint.UNDERLINE_TEXT_FLAG.inv()
    }
}

// TODO: set alpha value
@BindingAdapter("setCardViewColor")
fun setCardViewColor(materialCardView: MaterialCardView, @ColorRes color: Int) {
    val colorInt = materialCardView.context.getColor(color)
    val alphaColor = ColorUtils.setAlphaComponent(colorInt, 10)
    materialCardView.setCardBackgroundColor(alphaColor)
}

@BindingAdapter("customFocusChangeListener")
fun customFocusChangeListener(view: ImageButton, listener: (Boolean) -> Unit) {
    view.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            view.clearFocus()
        }
        listener.invoke(hasFocus)
    }
}

@BindingAdapter("layoutBackgroundColor")
fun layoutBackgroundColor(view: Button, @ColorRes color: Int) {
    view.backgroundTintList = view.context.getColorStateList(color)
}

@BindingAdapter("isSelected")
fun setCardViewBorder(view: MaterialCardView, isSelected: Boolean) {
    if (isSelected) {
        view.strokeWidth = pxToDp(20)
        view.strokeColor =
            ResourcesCompat.getColor(view.resources, R.color.white, view.context.theme)
    } else {
        view.strokeWidth = pxToDp(0)
    }
}

@BindingAdapter("setAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: BaseListAdapter<BaseListItem, ViewDataBinding>?
) {
    adapter?.let {
        recyclerView.adapter = it
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("submitList")
fun submitList(recyclerView: RecyclerView, list: List<BaseListItem>?) {
    val adapter = recyclerView.adapter as BaseListAdapter<BaseListItem, ViewDataBinding>?
    adapter?.setItems(list ?: listOf())
}

fun pxToDp(px: Int): Int {
    return (px / Resources.getSystem().displayMetrics.density).toInt()
}

@BindingConversion
fun convertColorToDrawable(color: Int): ColorDrawable? {
    return if (color != 0) ColorDrawable(color) else null
}