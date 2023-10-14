package com.duhapp.dnotes.features.note.ui

import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.duhapp.dnotes.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("lockIcon")
fun lockIcon(view: ImageButton, editableModeEnabled: Boolean) {
    if (!editableModeEnabled) {
        view.setImageResource(R.drawable.baseline_edit_24)
    } else {
        view.setImageResource(R.drawable.baseline_edit_off_24)
    }
}

@BindingAdapter("lockIcon")
fun lockIcon(view: FloatingActionButton, editableModeEnabled: Boolean) {
    if (!editableModeEnabled) {
        view.setImageResource(R.drawable.baseline_edit_24)
    } else {
        view.setImageResource(R.drawable.baseline_edit_off_24)
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

@BindingAdapter("setBackgroundAsCategoryLightColor")
fun setBackgroundAsCategoryLightColor(view: ConstraintLayout, uiState: NoteUIState) {
    view.setBackgroundColor(
        NoteUIStateFunctions.getSuccessStateData(uiState)?.baseNoteUIModel?.category?.color?.color?.lightColor ?:
        run { R.color.note_color_red_light }
    )
}