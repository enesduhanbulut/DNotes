package com.duhapp.dnotes.features.all_notes.ui

import android.view.View
import android.widget.CheckBox
import androidx.databinding.BindingAdapter

@BindingAdapter("visibility")
fun setVisibility(view: View, selectable: Boolean) {
    view.visibility = if (selectable) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("isSelected")
fun setSelected(view: CheckBox, isSelected: Boolean) {
    view.isChecked = isSelected
}
