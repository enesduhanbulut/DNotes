package com.duhapp.dnotes.features.all_notes.domain

import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.note.data.NoteRepository

class DeleteNote(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(noteUIModel: List<BaseNoteUIModel>) {
        if (noteUIModel.isNotEmpty())
            noteRepository.deleteNotes(noteUIModel)
    }
}