package com.duhapp.dnotes.features.all_notes.domain

import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionCode
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.note.data.NoteRepository

class DeleteNote(
    private val noteRepository: NoteRepository
) {
    suspend operator fun invoke(noteUIModel: List<BaseNoteUIModel>) {

        if (noteUIModel.any { it.id < 0 }) {
            throw CustomException.NotValidParametersException(
                CustomExceptionData(
                    title = R.string.Not_Valid_Parameters,
                    message = R.string.Note_Id_Could_Not_Be_Less_Than_Zero,
                    code = CustomExceptionCode.NOT_VALID_PARAMETERS_EXCEPTION.code
                )
            )
        }

        if (noteUIModel.isNotEmpty())
            noteRepository.deleteNotes(noteUIModel)
    }
}