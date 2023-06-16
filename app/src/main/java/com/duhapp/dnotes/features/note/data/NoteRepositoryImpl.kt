package com.duhapp.dnotes.features.note.data

import com.duhapp.dnotes.app.database.NoteDao
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
) : NoteRepository {

    override suspend fun insert(noteUIModel: BaseNoteUIModel): Int =
        noteDao.insert(noteUIModel.toEntity()).toInt()

    override suspend fun update(noteUIModel: BaseNoteUIModel) =
        noteDao.update(noteUIModel.toEntity())

    override suspend fun getNoteByCategory(category: CategoryUIModel): List<BaseNoteUIModel> =
        noteDao.getNoteByCategoryId(category.id).map {
            it.toUIModel(category)
        }
}
