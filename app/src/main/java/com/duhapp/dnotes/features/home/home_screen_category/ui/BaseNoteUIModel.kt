package com.duhapp.dnotes.features.home.home_screen_category.ui

import android.os.Parcelable
import androidx.annotation.ColorRes
import com.duhapp.dnotes.app.database.NoteEntity
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import kotlinx.parcelize.Parcelize

@Parcelize
open class BaseNoteUIModel(
    open var id: Int,
    open val type: NoteType,
    open var category: CategoryUIModel,
    open var title: String,
    open var body: String,
    @ColorRes open val color: Int,
    open val image: String,
    open val isPinned: Boolean,
    open val isCompletable: Boolean,
    open val isCompleted: Boolean,
) : Parcelable {
    open fun newCopy(): BaseNoteUIModel {
        return BaseNoteUIModel(
            id = id,
            type = type,
            category = category,
            title = title,
            body = body,
            color = color,
            image = image,
            isPinned = isPinned,
            isCompletable = isCompletable,
            isCompleted = isCompleted,
        )
    }

    open fun toEntity(): NoteEntity {
        return NoteEntity(
            id = id,
            categoryId = category.id,
            title = title,
            details = body,
        )
    }
}

@Parcelize
sealed class NoteType(
    val id: Int,
) : Parcelable {
    object BasicNote : NoteType(0)
    object ImageNote : NoteType(1)
    data class SubNote(val list: List<BaseNoteUIModel>) : NoteType(2)
    data class CheckList(val list: List<CheckListItem>) : NoteType(3)
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

    override fun newCopy(): BasicNoteUIModel {
        return BasicNoteUIModel(
            id = id,
            category = category,
            title = title,
            body = body,
            color = color,
            isPinned = isPinned,
            isCompletable = isCompletable,
            isCompleted = isCompleted,
        )
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

    override fun newCopy(): ImageNoteUIModel {
        return ImageNoteUIModel(
            id = id,
            category = category,
            title = title,
            body = body,
            color = color,
            isPinned = isPinned,
            isCompletable = isCompletable,
            isCompleted = isCompleted,
            image = image,
        )
    }
}
