package com.duhapp.dnotes.features.all_notes.ui

import android.view.View
import android.widget.CheckBox
import androidx.databinding.BindingAdapter
import com.duhapp.dnotes.R

@BindingAdapter("visibility")
fun setVisibility(view: View, selectable: Boolean) {
    view.visibility = if (selectable) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("border")
fun setBorder(view: View, selectable: Boolean) {
    view.setBackgroundResource(
        if (selectable)
            R.drawable.note_item_selected_border
        else
            R.drawable.note_item_unselected_border
    )
}

