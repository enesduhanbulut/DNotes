package com.duhapp.dnotes.features.note.ui

import android.widget.EditText
import androidx.databinding.BindingAdapter


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
