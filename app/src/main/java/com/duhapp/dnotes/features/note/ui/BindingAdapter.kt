package com.duhapp.dnotes.features.note.ui

import android.text.InputType
import android.widget.EditText
import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import com.duhapp.dnotes.R

@BindingAdapter("lockIcon")
fun lockIcon(view: ImageButton, isLocked: Boolean) {
    if (isLocked) {
        view.setImageResource(R.drawable.edit)
    } else {
        view.setImageResource(R.drawable.save)
    }
}

@BindingAdapter("isLocked")
fun lockIcon(view: EditText, isLocked: Boolean) {
    if (isLocked) {
        view.inputType = InputType.TYPE_NULL
    } else {
        view.inputType = InputType.TYPE_CLASS_TEXT
    }
}
