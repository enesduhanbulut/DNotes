package com.duhapp.dnotes.features.all_notes.domain

import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.note.data.NoteRepository

class UpdateNotes (private val noteRepository: NoteRepository) {
    suspend operator fun invoke(notes: List<BaseNoteUIModel>) {
        if (notes.any { it.title.isEmpty() })
            throw CustomException.InvalidInputException(
                CustomExceptionData(
                    title = R.string.Invalid_Input,
                    message = R.string.Note_Title_Cannot_Be_Empty,
                    code = 0
                )
            )

        if(notes.isNotEmpty())
            noteRepository.updateNotes(notes)
    }
}