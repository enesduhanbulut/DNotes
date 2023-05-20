package com.duhapp.dnotes.features.note.domain

import com.duhapp.dnotes.features.note.data.NoteRepository
import com.duhapp.dnotes.features.note.ui.NoteUIModel

class UpsertNote(
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(noteModel: NoteUIModel): NoteUIModel {
        return noteRepository.upsert(noteModel)
    }
}
