package com.duhapp.dnotes.features.note.ui

import com.duhapp.dnotes.app.database.NoteEntity
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel

data class NoteUIModel(
    var id: Int = -1,
    var title: String,
    var details: String,
    var categoryUIModel: CategoryUIModel,
) {
    fun toEntity() = NoteEntity(
        title = title,
        details = details,
        categoryId = categoryUIModel.id,
    )
}
