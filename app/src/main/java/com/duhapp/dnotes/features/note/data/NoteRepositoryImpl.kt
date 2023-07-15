package com.duhapp.dnotes.features.note.data

import com.duhapp.dnotes.app.database.NoteDao
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.data.BaseRepository
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import kotlinx.coroutines.CoroutineDispatcher

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    dispatchers: CoroutineDispatcher,
) : NoteRepository, BaseRepository(dispatchers) {

    override suspend fun insert(noteUIModel: BaseNoteUIModel): Int = runOnIO {
        noteDao.insert(noteUIModel.toEntity()).toInt()
    }


    override suspend fun update(noteUIModel: BaseNoteUIModel) = runOnIO {
        noteDao.update(noteUIModel.toEntity())
    }

    override suspend fun updateNotes(notes: List<BaseNoteUIModel>) = runOnIO {
        noteDao.updateNotes(notes.map { it.toEntity() })
    }

    override suspend fun getNoteByCategory(category: CategoryUIModel): List<BaseNoteUIModel> =
        runOnIO {
            noteDao.getNoteByCategoryId(category.id).map {
                it.toUIModel(category)
            }
        }

    override suspend fun deleteNotes(notes: List<BaseNoteUIModel>) = runOnIO {
        noteDao.deleteNotes(notes.map { it.id })
    }
}
