package com.duhapp.dnotes.features.add_or_update_category.ui

import android.os.Parcelable
import com.duhapp.dnotes.NoteColor
import kotlinx.parcelize.Parcelize

@Parcelize
data class ColorItemUIModel(
    val color: NoteColor
) : Parcelable