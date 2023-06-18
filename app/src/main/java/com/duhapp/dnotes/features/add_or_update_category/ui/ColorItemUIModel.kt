package com.duhapp.dnotes.features.add_or_update_category.ui

import android.os.Parcelable
import com.duhapp.dnotes.NoteColor
import com.duhapp.dnotes.features.base.ui.BaseListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ColorItemUIModel(
    var isSelected: Boolean = false,
    val color: NoteColor
) : Parcelable, BaseListItem