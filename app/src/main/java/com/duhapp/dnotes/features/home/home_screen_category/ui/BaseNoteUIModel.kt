package com.duhapp.dnotes.features.home.home_screen_category.ui

import android.os.Parcelable
import androidx.annotation.ColorRes
import com.duhapp.dnotes.app.database.NoteEntity
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.BaseListItem
import kotlinx.parcelize.Parcelize

@Parcelize
open class BaseNoteUIModel(
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
    var isSelected: Boolean = false,
    var isSelectable: Boolean = false,
) : Cloneable, BaseListItem, Parcelable {
    open fun newCopy(): BaseNoteUIModel {
        return super.clone() as BaseNoteUIModel
    }

    open fun toEntity(): NoteEntity {
        return NoteEntity(
            id = id,
            title = title,
            details = body,
            categoryId = category.id,
        )
    }
}

sealed class NoteType(
    val id: Int,
) : Parcelable {
    @Parcelize
    object BasicNote : NoteType(0), Parcelable

    @Parcelize
    object ImageNote : NoteType(1), Parcelable

    @Parcelize
    data class SubNote(val list: List<BaseNoteUIModel>) : NoteType(2), Parcelable

    @Parcelize
    data class CheckList(val list: List<CheckListItem>) : NoteType(3), Parcelable
}

@Parcelize
data class CheckListItem(
    val id: Int,
    val isChecked: Boolean,
    val text: String,
    val subList: List<CheckListItem>,
    @ColorRes val color: Int,
) : Parcelable

class BasicNoteUIModel(
    id: Int,
    isPinned: Boolean,
    isCompleted: Boolean,
    isCompletable: Boolean,
    category: CategoryUIModel,
    title: String,
    body: String,
    @ColorRes color: Int,
    isSelected: Boolean = false,
    isSelectable: Boolean = false,
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
    isSelected = isSelected,
    isSelectable = isSelectable,
) {
    override fun toEntity() = NoteEntity(
        id = id,
        title = title,
        details = body,
        categoryId = category.id,
    )

    override fun newCopy(): BasicNoteUIModel {
        return super.clone() as BasicNoteUIModel
    }
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
    isSelected: Boolean = false,
    isSelectable: Boolean = false,
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
    isSelected = isSelected,
    isSelectable = isSelectable,
) {
    override fun toEntity() = NoteEntity(
        id = id,
        title = title,
        details = body,
        categoryId = category.id,
    )

    override fun newCopy(): ImageNoteUIModel {
        return super.clone() as ImageNoteUIModel
    }
}
