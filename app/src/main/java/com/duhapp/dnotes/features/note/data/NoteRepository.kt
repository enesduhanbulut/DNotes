package com.duhapp.dnotes.features.note.data

import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel

interface NoteRepository {
    suspend fun insert(noteUIModel: BaseNoteUIModel): Int
    suspend fun update(noteUIModel: BaseNoteUIModel)
    suspend fun updateNotes(notes: List<BaseNoteUIModel>)
    suspend fun getNoteByCategory(category: CategoryUIModel): List<BaseNoteUIModel>
    suspend fun deleteNotes(notes: List<BaseNoteUIModel>)
}
