package com.duhapp.dnotes.features.all_notes.domain

import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionCode
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.note.data.NoteRepository

class UpdateNotes (private val noteRepository: NoteRepository) {
    suspend operator fun invoke(notes: List<BaseNoteUIModel>) {
        if(notes.isNotEmpty())
            noteRepository.updateNotes(notes)
        else
            throw CustomException.WrongParametersException(
                CustomExceptionData(
                    title = R.string.Error_There_Is_No_Suitable_Variable,
                    message = R.string.Notes_Could_Not_Be_Empty,
                    code = CustomExceptionCode.WRONG_PARAMETERS_EXCEPTION.code
                )
            )
    }
}