package com.duhapp.dnotes.features.note.data

import com.duhapp.dnotes.features.note.ui.NoteUIModel

interface NoteRepository {

    suspend fun upsert(noteUIModel: NoteUIModel): NoteUIModel
}
