package com.duhapp.dnotes.features.note.ui

import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.databinding.BindingAdapter
import com.duhapp.dnotes.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("lockIcon")
fun lockIcon(view: ImageButton, isLocked: Boolean) {
    if (isLocked) {
        view.setImageResource(R.drawable.edit)
    } else {
        view.setImageResource(R.drawable.save)
    }
}

@BindingAdapter("lockIcon")
fun lockIcon(view: FloatingActionButton, isLocked: Boolean) {
    if (isLocked) {
        view.setImageResource(R.drawable.edit)
    } else {
        view.setImageResource(R.drawable.save)
    }
}

@BindingAdapter("isLocked")
fun setEditable(view: EditText, isLocked: Boolean) {
    view.isEnabled = !isLocked
}

@BindingAdapter("isLocked")
fun setClickable(view: RelativeLayout, isLocked: Boolean) {
    view.isClickable = !isLocked
}
