package com.duhapp.dnotes.features.home.home_screen_category.ui

import android.os.Parcelable
import androidx.annotation.ColorRes
import com.duhapp.dnotes.NoteColor
import com.duhapp.dnotes.app.database.NoteEntity
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.add_or_update_category.ui.ColorItemUIModel
import com.duhapp.dnotes.features.base.ui.BaseListItem
import kotlinx.parcelize.Parcelize

var DEFAULT_NOTE_MODEL: BaseNoteUIModel = BasicNoteUIModel(
    id = -1,
    isPinned = false,
    isCompleted = false,
    isCompletable = false,
    title = "",
    body = "",
    category = CategoryUIModel(
        id = -1,
        name = "",
        emoji = "",
        description = "",
        color = ColorItemUIModel(
            color = NoteColor.BLUE,
        )
    ),
    color = -1,
)

@Parcelize
open class BaseNoteUIModel(
    var id: Int,
    val type: NoteType,
    var category: CategoryUIModel,
    var title: String,
    var body: String,
    @ColorRes var color: Int,
    var image: String,
    var isPinned: Boolean,
    var isCompletable: Boolean,
    var isCompleted: Boolean,
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

    override fun equals(other: Any?): Boolean {
        if (other is BaseNoteUIModel) {
            return other.id == id &&
                    other.type == type &&
                    other.category == category &&
                    other.title == title &&
                    other.body == body &&
                    other.color == color &&
                    other.image == image &&
                    other.isPinned == isPinned &&
                    other.isCompletable == isCompletable &&
                    other.isCompleted == isCompleted &&
                    other.isSelected == isSelected &&
                    other.isSelectable == isSelectable
        }
        return false
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + type.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + color
        result = 31 * result + image.hashCode()
        result = 31 * result + isPinned.hashCode()
        result = 31 * result + isCompletable.hashCode()
        result = 31 * result + isCompleted.hashCode()
        result = 31 * result + isSelected.hashCode()
        result = 31 * result + isSelectable.hashCode()
        return result

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

    override fun toString(): String {
        return "BasicNoteUIModel(id=$id, type=$type, category=$category, title='$title', body='$body', color=$color, image='$image', isPinned=$isPinned, isCompletable=$isCompletable, isCompleted=$isCompleted, isSelected=$isSelected, isSelectable=$isSelectable)"
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
