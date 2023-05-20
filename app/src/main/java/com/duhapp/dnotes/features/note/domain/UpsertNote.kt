package com.duhapp.dnotes.features.note.domain

import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.note.data.NoteRepository

class UpsertNote(
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(noteModel: BaseNoteUIModel): Int =
        if (noteModel.id == -1) {
            noteModel.id = 0
            noteRepository.insert(noteModel)
        } else {
            noteRepository.update(noteModel)
            noteModel.id
        }
}
