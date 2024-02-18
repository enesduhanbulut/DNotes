package com.duhapp.dnotes.features.note.data

import com.duhapp.dnotes.R
import com.duhapp.dnotes.app.database.NoteDao
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.data.BaseRepository
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionCode
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    dispatchers: CoroutineDispatcher,
) : NoteRepository, BaseRepository(dispatchers) {

    override suspend fun insert(noteUIModel: BaseNoteUIModel): Int = runOnIO {
        try {
            noteDao.insert(noteUIModel.toEntity()).toInt()
        } catch (e: Exception) {
            Timber.e(e)
            throw CustomException.DatabaseException(
                CustomExceptionData(
                    title = R.string.Error_Database,
                    message = R.string.Error_While_Inserting_Note,
                    code = CustomExceptionCode.DATABASE_EXCEPTION.code
                )
            )
        }
    }


    override suspend fun update(noteUIModel: BaseNoteUIModel) = runOnIO {
        try {
            noteDao.update(noteUIModel.toEntity())
        } catch (e: Exception) {
            Timber.e(e)
            throw CustomException.DatabaseException(
                CustomExceptionData(
                    title = R.string.Error_Database,
                    message = R.string.Error_While_Updating_Note,
                    code = CustomExceptionCode.DATABASE_EXCEPTION.code
                )
            )
        }
    }

    override suspend fun updateNotes(notes: List<BaseNoteUIModel>) = runOnIO {
        try {
            noteDao.updateNotes(notes.map { it.toEntity() })
        } catch (e: Exception) {
            Timber.e(e)
            throw CustomException.DatabaseException(
                CustomExceptionData(
                    title = R.string.Error_Database,
                    message = R.string.Error_While_Updating_Note,
                    code = CustomExceptionCode.DATABASE_EXCEPTION.code
                )
            )
        }
    }

    override suspend fun getNoteByCategory(categoryId: CategoryUIModel): List<BaseNoteUIModel> =
        runOnIO {
            try {
                noteDao.getNoteByCategoryId(categoryId.id).map {
                    it.toUIModel(categoryId)
                }
            } catch (e: Exception) {
                Timber.e(e)
                throw CustomException.DatabaseException(
                    CustomExceptionData(
                        title = R.string.Error_Database,
                        message = R.string.Error_While_Fetching_Note,
                        code = CustomExceptionCode.DATABASE_EXCEPTION.code
                    )
                )
            }
        }

    override suspend fun deleteNotes(notes: List<BaseNoteUIModel>) = runOnIO {
        try {
            noteDao.deleteNotes(notes.map { it.id })
        } catch (e: Exception) {
            Timber.e(e)
            throw CustomException.DatabaseException(
                CustomExceptionData(
                    title = R.string.Error_Database,
                    message = R.string.Error_While_Deleting_Note,
                    code = CustomExceptionCode.DATABASE_EXCEPTION.code
                )
            )
        }
    }
}
