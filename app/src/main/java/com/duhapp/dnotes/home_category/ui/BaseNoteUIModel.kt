package com.duhapp.dnotes.home_category.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

abstract class BaseNoteUIModel(
    val id: Int,
    val type: NoteType,
    val category: NoteCategory,
    val isPinned: Boolean,
    val isCompletable: Boolean,
    val isCompleted: Boolean
)

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
    val subList: List<CheckListItem>
)
data class NoteCategory(
    val id: Int,
    val title: String,
    @ColorRes val color: Int,
    @DrawableRes val icon: Int,
)

class BasicNoteUIModel(
    id: Int,
    type: NoteType,
    category: NoteCategory,
    isPinned: Boolean,
    isCompleted: Boolean,
    isCompletable: Boolean,
    val title: String,
    val body: String,

    ) : BaseNoteUIModel(id, type, category, isPinned, isCompleted, isCompletable)

class ImageNoteUIModel(
    id: Int,
    type: NoteType,
    category: NoteCategory,
    isPinned: Boolean,
    isCompleted: Boolean,
    isCompletable: Boolean,
    val title: String,
    val body: String,
    val image: String,
) : BaseNoteUIModel(id, type, category, isPinned, isCompleted, isCompletable)


