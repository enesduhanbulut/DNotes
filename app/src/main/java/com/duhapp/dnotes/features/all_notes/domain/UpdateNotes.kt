package com.duhapp.dnotes.features.all_notes.domain

import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.note.data.NoteRepository

class UpdateNotes (val noteRepository: NoteRepository) {
    suspend operator fun invoke(notes: List<BaseNoteUIModel>) {
        if(notes.isNotEmpty())
            noteRepository.updateNotes(notes)
    }
}