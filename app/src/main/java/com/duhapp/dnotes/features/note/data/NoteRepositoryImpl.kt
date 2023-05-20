package com.duhapp.dnotes.features.note.data

import com.duhapp.dnotes.app.database.NoteDao
import com.duhapp.dnotes.features.note.ui.NoteUIModel

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
) : NoteRepository {
    override suspend fun upsert(noteUIModel: NoteUIModel): NoteUIModel {
        val id = noteDao.upsert(noteUIModel.toEntity())
        noteUIModel.id = id.toInt()
        return noteUIModel
    }
}
