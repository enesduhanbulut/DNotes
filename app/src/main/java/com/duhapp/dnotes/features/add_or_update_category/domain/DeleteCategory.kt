package com.duhapp.dnotes.features.add_or_update_category.domain

import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionCode
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.base.domain.asCustomException
import com.duhapp.dnotes.features.note.data.NoteRepository

class DeleteCategory(
    private val categoryRepository: CategoryRepository,
    private val noteRepository: NoteRepository,
    private val defaultCategoryModel: CategoryUIModel,
) {
    suspend operator fun invoke(categoryUIModel: CategoryUIModel) {
        if (categoryUIModel.id < 0) {
            throw CustomException.ThereIsNoSuitableVariableException(
                CustomExceptionData(
                    title = R.string.Error_There_Is_No_Suitable_Variable,
                    message = R.string.Default_Category_Could_Not_Be_Fetch,
                    code = CustomExceptionCode.THERE_IS_NO_SUITABLE_VARIABLE_EXCEPTION.code
                )
            )
        }
        if (defaultCategoryModel.id == categoryUIModel.id)
            throw CustomException.WrongParametersException(
                CustomExceptionData(
                    title = R.string.Category_Deleting_Error,
                    message = R.string.Default_Category_Could_Not_Be_Deleted,
                    code = CustomExceptionCode.WRONG_PARAMETERS_EXCEPTION.code
                )
            )
        else {
            try {
                val categoryNotes = noteRepository.getNoteByCategory(categoryUIModel)
                categoryRepository.deleteCategory(categoryUIModel, categoryNotes)
                noteRepository.getNoteByCategory(categoryUIModel)
                    .map { note ->
                        note.newCopy().apply {
                            this.category = defaultCategoryModel
                        }
                    }.let {
                        noteRepository.updateNotes(it)
                    }
            } catch (e: Exception) {
                throw e.asCustomException(
                        title = R.string.Unknown_Error,
                        message = R.string.Error_While_Deleting_Category,
                        code = CustomExceptionCode.UNKNOWN_EXCEPTION.code
                )
            }
        }
    }
}
