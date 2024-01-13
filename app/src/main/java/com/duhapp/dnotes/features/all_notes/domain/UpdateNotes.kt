package com.duhapp.dnotes.features.all_notes.domain

import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionCode
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.note.data.NoteRepository

class UpdateNotes (private val noteRepository: NoteRepository) {
    suspend operator fun invoke(notes: List<BaseNoteUIModel>) {
        if (notes.any { it.id < 0 })
            throw CustomException.NotValidParametersException(
                CustomExceptionData(
                    title = R.string.Not_Valid_Parameters,
                    message = R.string.Note_Id_Could_Not_Be_Less_Than_Zero,
                    code = CustomExceptionCode.NOT_VALID_PARAMETERS_EXCEPTION.code
                )
            )

        if (notes.isEmpty())
            throw CustomException.NotValidParametersException(
                CustomExceptionData(
                    title = R.string.Error_There_Is_No_Suitable_Variable,
                    message = R.string.Notes_Could_Not_Be_Empty,
                    code = CustomExceptionCode.NOT_VALID_PARAMETERS_EXCEPTION.code
                )
            )

        noteRepository.updateNotes(notes)
    }
}