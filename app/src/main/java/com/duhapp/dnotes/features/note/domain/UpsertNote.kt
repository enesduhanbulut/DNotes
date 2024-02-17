package com.duhapp.dnotes.features.note.domain

import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.note.data.NoteRepository

class UpsertNote(
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(noteModel: BaseNoteUIModel): BaseNoteUIModel {
        if (noteModel.title.isEmpty())
            throw CustomException.InvalidInputException(
                CustomExceptionData(
                    title = R.string.Invalid_Input,
                    message = R.string.Note_Title_Cannot_Be_Empty,
                    code = 0,
                )
            )
        return if (noteModel.id == -1) {
            noteModel.id = 0
            noteModel.id = noteRepository.insert(noteModel)
            noteModel
        } else {
            noteRepository.update(noteModel)
            noteModel
        }
    }
}
