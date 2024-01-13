package com.duhapp.dnotes.features.all_notes.domain

import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionCode
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.note.data.NoteRepository

class GetNotesByCategoryId(
    private val noteRepository: NoteRepository, private val categoryRepository: CategoryRepository
) {
    suspend fun invoke(categoryId: Int): List<BaseNoteUIModel> {
        if (categoryId < 0)
            throw CustomException.NotValidParametersException(
                CustomExceptionData(
                    title = R.string.Not_Valid_Parameters,
                    message = R.string.Category_Id_Cannot_Be_Minus_Val,
                    code = CustomExceptionCode.NOT_VALID_PARAMETERS_EXCEPTION.code
                )
            )
        val category = categoryRepository.getById(categoryId)
        return noteRepository.getNoteByCategory(category ?: return emptyList())
    }
}