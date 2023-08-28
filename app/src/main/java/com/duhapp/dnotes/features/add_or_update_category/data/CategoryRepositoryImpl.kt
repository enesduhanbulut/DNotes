package com.duhapp.dnotes.features.add_or_update_category.data

import com.duhapp.dnotes.NoteColor
import com.duhapp.dnotes.R
import com.duhapp.dnotes.app.database.CategoryDao
import com.duhapp.dnotes.app.database.CategoryEntity
import com.duhapp.dnotes.app.database.NoteDao
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.add_or_update_category.ui.ColorItemUIModel
import com.duhapp.dnotes.features.base.data.BaseRepository
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionCode
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.base.domain.asCustomException
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val noteDao: NoteDao,
    dispatcher: CoroutineDispatcher
) : CategoryRepository, BaseRepository(dispatcher) {
    private var lastDeletedCategory: CategoryUIModel? = null
    private var lastMovedCategoryNotes: List<BaseNoteUIModel>? = null

    override suspend fun deleteCategory(categoryUIModel: CategoryUIModel, categoryNotes: List<BaseNoteUIModel>) {
        runOnIO {
            try {
                categoryDao.deleteCategoryWithId(categoryUIModel.id)
                lastDeletedCategory = categoryUIModel
                lastMovedCategoryNotes = categoryNotes
            } catch (e: Exception) {
                Timber.e(e)
                throw CustomException.DatabaseException(
                    CustomExceptionData(
                        title = R.string.Error_Database,
                        message = R.string.Error_While_Deleting_Category,
                        code = CustomExceptionCode.DATABASE_EXCEPTION.code
                    )
                )
            }
        }
    }

    override suspend fun insert(categoryUIModel: CategoryUIModel): Int {
        val category = categoryUIModel.toEntity()
        var id: Long = -1
        runOnIO {
            try {
                id = categoryDao.insert(category)
            } catch (e: Exception) {
                Timber.e(e)
                throw CustomException.DatabaseException(
                    CustomExceptionData(
                        title = R.string.Error_Database,
                        message = R.string.Error_While_Inserting_Category,
                        code = CustomExceptionCode.DATABASE_EXCEPTION.code
                    )
                )
            }
        }
        return id.toInt()
    }

    override suspend fun getById(id: Int): CategoryUIModel? = runOnIO {
        try {
            categoryDao.getById(id)?.toUIModel()
        } catch (e: Exception) {
            Timber.e(e)
            throw CustomException.DatabaseException(
                CustomExceptionData(
                    title = R.string.Error_Database,
                    message = R.string.Error_While_Fetching_Category,
                    code = CustomExceptionCode.DATABASE_EXCEPTION.code
                )
            )
        }
    }

    override suspend fun undo(): Boolean {
        if (lastDeletedCategory == null)
            throw CustomException.UndoUnavailableException(
                CustomExceptionData(R.string.undo_unavailable, R.string.undo_unavailable, -1)
            )
        try {
            val categoryId = insert(lastDeletedCategory!!)
            lastDeletedCategory!!.id = categoryId
            runOnIO {
                lastMovedCategoryNotes?.mapTo(mutableListOf()) { note ->
                    note.newCopy().apply {
                        this.category = lastDeletedCategory!!
                    }
                    note.toEntity()
                }?.let { noteEntities ->
                    noteDao.updateNotes(noteEntities)
                }
            }
            clearLastDeletedCategory()
        } catch (e: Exception) {
            Timber.e("Undo process didn't succeeded, ${lastDeletedCategory.toString()} could not be inserted")
            clearLastDeletedCategory()
            throw CustomException.DatabaseException(
                CustomExceptionData(
                    title = R.string.Error_Database,
                    message = R.string.Error_While_Inserting_Category,
                    code = CustomExceptionCode.DATABASE_EXCEPTION.code
                )
            )
        }
        return true
    }

    override suspend fun updateCategory(categoryUIModel: CategoryUIModel) {
        val category = categoryUIModel.toEntity()
        category.id = categoryUIModel.id
        runOnIO {
            try {
                categoryDao.update(category)
            } catch (e: Exception) {
                Timber.e(e)
                throw CustomException.DatabaseException(
                    CustomExceptionData(
                        title = R.string.Error_Database,
                        message = R.string.Error_While_Updating_Category,
                        code = CustomExceptionCode.DATABASE_EXCEPTION.code
                    )
                )
            }
        }
    }

    override suspend fun getCategories(): List<CategoryUIModel> = runOnIO {
        try {
            categoryDao.getCategories().map { categoryEntity ->
                categoryEntity.toUIModel()
            }
        } catch (e: Exception) {
            Timber.e(e)
            throw CustomException.DatabaseException(
                CustomExceptionData(
                    title = R.string.Error_Database,
                    message = R.string.Error_While_Fetching_Category,
                    code = CustomExceptionCode.DATABASE_EXCEPTION.code
                )
            )
        }
    }
    private fun clearLastDeletedCategory() {
        lastDeletedCategory = null
        lastMovedCategoryNotes = null
    }
}