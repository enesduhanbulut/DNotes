package com.duhapp.dnotes.features.home.home_screen_category.ui

import androidx.annotation.ColorRes
import com.duhapp.dnotes.app.database.NoteEntity
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel

abstract class BaseNoteUIModel(
    var id: Int,
    val type: NoteType,
    var category: CategoryUIModel,
    var title: String,
    var body: String,
    @ColorRes val color: Int,
    val image: String,
    val isPinned: Boolean,
    val isCompletable: Boolean,
    val isCompleted: Boolean,
) {
    abstract fun toEntity(): NoteEntity
}

sealed interface NoteType {
    object BasicNote : NoteType
    object ImageNote : NoteType
    data class SubNote(val list: List<BaseNoteUIModel>) : NoteType
    data class CheckList(val list: List<CheckListItem>) : NoteType
}

data class CheckListItem(
    val id: Int,
    val isChecked: Boolean,
    val text: String,
    val subList: List<CheckListItem>,
    @ColorRes val color: Int,
)

class BasicNoteUIModel(
    id: Int,
    isPinned: Boolean,
    isCompleted: Boolean,
    isCompletable: Boolean,
    category: CategoryUIModel,
    title: String,
    body: String,
    @ColorRes color: Int,
) : BaseNoteUIModel(
    id = id,
    type = NoteType.BasicNote,
    isPinned = isPinned,
    isCompleted = isCompleted,
    category = category,
    isCompletable = isCompletable,
    title = title,
    body = body,
    image = "",
    color = color,
) {
    override fun toEntity() = NoteEntity(
        id = id,
        title = title,
        details = body,
        categoryId = category.id,
    )
}

class ImageNoteUIModel(
    id: Int,
    isPinned: Boolean,
    isCompleted: Boolean,
    isCompletable: Boolean,
    category: CategoryUIModel,
    title: String,
    body: String,
    image: String,
    @ColorRes color: Int,
) : BaseNoteUIModel(
    id = id,
    type = NoteType.BasicNote,
    isPinned = isPinned,
    isCompleted = isCompleted,
    category = category,
    isCompletable = isCompletable,
    title = title,
    body = body,
    image = image,
    color = color,
) {
    override fun toEntity() = NoteEntity(
        id = id,
        title = title,
        details = body,
        categoryId = category.id,
    )
}
