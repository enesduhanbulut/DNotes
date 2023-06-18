package com.duhapp.dnotes.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BasicNoteUIModel

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo("title") val title: String,
    @ColumnInfo(name = "details") val details: String,
    @ColumnInfo(name = "category_id") val categoryId: Int,
) {
    fun toUIModel(category: CategoryUIModel): BaseNoteUIModel = BasicNoteUIModel(
        id = id,
        title = title,
        body = details,
        category = category,
        color = category.color.color.ordinal,
        isPinned = false,
        isCompletable = false,
        isCompleted = false,
    )
}