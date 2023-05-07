package com.duhapp.dnotes

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

