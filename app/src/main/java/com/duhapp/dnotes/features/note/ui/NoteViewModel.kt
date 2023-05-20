package com.duhapp.dnotes.features.note.ui

import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.BaseViewModel
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.note.domain.UpsertNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val upsertNote: UpsertNote,
) : BaseViewModel<NoteUIEvent, NoteUIState>() {

    fun initUIState(categoryUIModel: CategoryUIModel) {
        mutableUIState.value = NoteUIState(
            noteUIModel = NoteUIModel(
                id = -1,
                title = "",
                details = "",
                categoryUIModel = categoryUIModel,
            ),
        )
    }

    fun switchEditMode() {
        viewModelScope.launch {
            mutableUIState.value?.let {
                var noteUIModel = it.noteUIModel
                if (mutableUIState.value?.editableMode == false) {
                    noteUIModel = upsertNote.invoke(noteUIModel)
                }
                mutableUIState.value =
                    it.copy(editableMode = !it.editableMode, noteUIModel = noteUIModel)
            }
        }
    }
}

sealed interface NoteUIEvent : FragmentUIEvent

data class NoteUIState(
    var noteUIModel: NoteUIModel = NoteUIModel(
        id = -1,
        title = "",
        details = "",
        categoryUIModel = CategoryUIModel(
            id = 0,
            name = "",
            emoji = "",
            description = "",
            colorId = 0,
        ),
    ),
    var editableMode: Boolean = false,
) : FragmentUIState
