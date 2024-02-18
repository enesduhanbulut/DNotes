package com.duhapp.dnotes.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duhapp.dnotes.NoteColor
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.add_or_update_category.ui.ColorItemUIModel

@Entity
data class CategoryEntity(
    @ColumnInfo(name = "title") val name: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "emoji") val emoji: String,
    @ColumnInfo(name = "color_id") val colorId: Int,
    @ColumnInfo(name = "is_default") val isDefault: Boolean = false
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0

    fun toUIModel() = CategoryUIModel(
        id = id,
        name = name,
        emoji = emoji,
        description = message,
        color = ColorItemUIModel(
            color = NoteColor.fromOrdinal(colorId)
        ),
        isDefault = isDefault
    )
}