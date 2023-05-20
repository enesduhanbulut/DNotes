package com.duhapp.dnotes.features.note.data

import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel

interface NoteRepository {
    suspend fun insert(noteUIModel: BaseNoteUIModel): Int

    suspend fun update(noteUIModel: BaseNoteUIModel)
}
