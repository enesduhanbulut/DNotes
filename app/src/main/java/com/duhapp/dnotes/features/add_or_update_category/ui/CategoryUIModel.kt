package com.duhapp.dnotes.features.add_or_update_category.ui

import android.os.Parcelable
import com.duhapp.dnotes.NoteColor
import com.duhapp.dnotes.app.database.CategoryEntity
import com.duhapp.dnotes.features.base.ui.BaseListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryUIModel(
    var id: Int = -1,
    var name: String = "",
    var emoji: String = "",
    var description: String = "",
    var color: ColorItemUIModel = ColorItemUIModel(
        color = NoteColor.fromOrdinal(0)
    ),
    var isDefault: Boolean = false
) : Parcelable, BaseListItem {
    fun toEntity() = CategoryEntity(
        name = name,
        emoji = emoji,
        message = description,
        colorId = color.color.ordinal,
        isDefault = isDefault
    )
}
