package com.duhapp.dnotes.features.note.domain

import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionCode
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.DEFAULT_NOTE_MODEL
import com.duhapp.dnotes.features.note.data.NoteRepository

class UpsertNote(
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(noteModel: BaseNoteUIModel): BaseNoteUIModel {
        if (noteModel.title.isEmpty() && noteModel.body.isEmpty()) {
            throw CustomException.ThereIsNoSuitableVariableException(
                CustomExceptionData(
                    title = R.string.Error_Note_Cannot_Be_Saved,
                    message = R.string.Error_Note_Info_Is_Empty,
                    code = CustomExceptionCode.THERE_IS_NO_SUITABLE_VARIABLE_EXCEPTION.code
                )
            )
        }

        if (noteModel.title.length > 180) {
            throw CustomException.ThereIsNoSuitableVariableException(
                CustomExceptionData(
                    title = R.string.Error_Note_Cannot_Be_Saved,
                    message = R.string.Error_Note_Info_Is_Too_Long,
                    code = CustomExceptionCode.THERE_IS_NO_SUITABLE_VARIABLE_EXCEPTION.code
                )
            )
        }
        if (noteModel.id == DEFAULT_NOTE_MODEL.id) {
            noteModel.id = 0
            noteModel.id = noteRepository.insert(noteModel)
        } else {
            noteRepository.update(noteModel)
        }
        return noteModel
    }

}
