package com.duhapp.dnotes.features.all_notes.ui

import android.view.View
import android.widget.TextView
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

@BindingAdapter("showShortText")
fun setCategoryTitleText(view: TextView, text: String) {
    view.text = if (text.length > 25) {
        text.substring(0, 25) + "..."
    } else {
        text
    }
}

@BindingAdapter("showMediumText")
fun setCategoryBodyText(view: TextView, text: String) {
    view.text = if (text.length > 40) {
        text.substring(0, 40) + "..."
    } else {
        text
    }
}

@BindingAdapter("showLongText")
fun setCategoryLongText(view: TextView, text: String) {
    view.text = if (text.length > 350) {
        text.substring(0, 350) + "..."
    } else {
        text
    }
}
