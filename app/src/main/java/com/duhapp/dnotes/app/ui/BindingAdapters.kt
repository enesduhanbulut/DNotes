package com.duhapp.dnotes.app.ui

import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.core.graphics.ColorUtils
import androidx.databinding.BindingAdapter
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

@BindingAdapter("layoutBackground")
fun layoutBackground(view: LinearLayout, @ColorRes color: Int) {
    view.backgroundTintList = view.context.getColorStateList(color)
}
