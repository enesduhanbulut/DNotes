package com.duhapp.dnotes.features.note.ui

import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.databinding.BindingAdapter
import com.duhapp.dnotes.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("lockIcon")
fun lockIcon(view: ImageButton, editableModeEnabled: Boolean) {
    if (!editableModeEnabled) {
        view.setImageResource(R.drawable.edit)
    } else {
        view.setImageResource(R.drawable.save)
    }
}

@BindingAdapter("lockIcon")
fun lockIcon(view: FloatingActionButton, editableModeEnabled: Boolean) {
    if (!editableModeEnabled) {
        view.setImageResource(R.drawable.edit)
    } else {
        view.setImageResource(R.drawable.save)
    }
}

@BindingAdapter("isLocked")
fun setEditable(view: EditText, editableModeEnabled: Boolean) {
    if (!editableModeEnabled) {
        view.isFocusable = false
        view.isFocusableInTouchMode = false
    } else {
        view.isFocusable = true
        view.isFocusableInTouchMode = true
    }
}

@BindingAdapter("isLocked")
fun setClickable(view: RelativeLayout, editableModeEnabled: Boolean) {
    view.isClickable = editableModeEnabled
}
