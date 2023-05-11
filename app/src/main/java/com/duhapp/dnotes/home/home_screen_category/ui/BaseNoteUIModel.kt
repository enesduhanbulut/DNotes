package com.duhapp.dnotes.home.home_screen_category.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

abstract class BaseNoteUIModel(
    val id: Int,
    val type: NoteType,
    val tag: NoteTag,
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
    val id: Int, val isChecked: Boolean, val text: String, val subList: List<CheckListItem>
)

data class NoteTag(
    val id: Int,
    val title: String,
    @ColorRes val color: Int,
    @DrawableRes val icon: Int,
)

class BasicNoteUIModel(
    id: Int,
    tag: NoteTag,
    isPinned: Boolean,
    isCompleted: Boolean,
    isCompletable: Boolean,
    val title: String,
    val body: String,

    ) : BaseNoteUIModel(id, NoteType.BasicNote, tag, isPinned, isCompleted, isCompletable)

class ImageNoteUIModel(
    id: Int,
    tag: NoteTag,
    isPinned: Boolean,
    isCompleted: Boolean,
    isCompletable: Boolean,
    val title: String,
    val body: String,
    val image: String,
) : BaseNoteUIModel(id, NoteType.BasicNote, tag, isPinned, isCompleted, isCompletable)


