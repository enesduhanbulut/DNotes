package com.duhapp.dnotes.features.all_notes.ui

import androidx.lifecycle.viewModelScope
import com.ahk.annotation.GenerateSealedGetters
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.all_notes.domain.DeleteNote
import com.duhapp.dnotes.features.all_notes.domain.GetNotesByCategoryId
import com.duhapp.dnotes.features.all_notes.domain.UpdateNotes
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.base.ui.FragmentViewModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllNotesViewModel @Inject constructor(
    private val getNotesByCategoryId: GetNotesByCategoryId,
    private val defaultCategoryModel: CategoryUIModel,
    private val deleteNote: DeleteNote,
    private val updateNotes: UpdateNotes,
) : FragmentViewModel<AllNotesEvent, AllNotesState>() {
    fun initiate(categoryId: Int) {
        setSuccessState(
            AllNotesState.Success(
                category = defaultCategoryModel, notes = emptyList()
            )
        )
        viewModelScope.launch {
            val notes = getNotesByCategoryId.invoke(categoryId)
            if (notes.isEmpty()) {

                // TODO: fix this
                /*setFailureState(
                    CustomException.ThereIsNoSuitableVariableException(
                        CustomExceptionData(
                            R.string.Data_Not_Found,
                            R.string.Note_Data_Not_Found,
                            -1,
                        )
                    )
                )*/
            } else {
                uiState.value?.let {

                }
            }
        }
    }

    fun onNoteClick(noteUIModel: BaseNoteUIModel) {
        val state = uiState.value ?: return

    }

    fun deleteSelectedNotes() {
        val state = uiState.value ?: return

    }


    fun onDeleteNoteClick(noteItem: BaseNoteUIModel) {

    }

    fun onEditNoteClick(baseNoteUIModel: BaseNoteUIModel) = setEvent(
        AllNotesEvent.OnEditNoteEvent(baseNoteUIModel)
    )

    fun onMoveNoteClick(baseNoteUIModel: BaseNoteUIModel) = setEvent(
        AllNotesEvent.OnMoveAnotherCategoryEvent(mutableListOf(baseNoteUIModel))
    )

    fun onMoveActionTriggered(
        noteUIModel: MutableList<BaseNoteUIModel>,
        category: CategoryUIModel
    ) {
        viewModelScope.launch {
            noteUIModel.forEach {
                it.category = category
            }
            updateNotes.invoke(noteUIModel)

        }
    }

    fun enableSelectionModeAndSelectANote(noteUIModel: BaseNoteUIModel) {
        val state = uiState.value ?: return

    }

    fun cancelSelectionMode() {
        uiState.value ?: return
        clearSelection()
    }

    fun moveSelectedNotes() {
        val state = uiState.value ?: return

    }

    private fun clearSelection() {
        val state = uiState.value ?: return

    }
}

sealed interface AllNotesEvent : FragmentUIEvent {
    object Idle : AllNotesEvent
    data class OnEditNoteEvent(val noteUIModel: BaseNoteUIModel) : AllNotesEvent
    data class OnMoveAnotherCategoryEvent(val noteUIModel: MutableList<BaseNoteUIModel>) :
        AllNotesEvent
}

@GenerateSealedGetters
sealed interface AllNotesState : FragmentUIState {
    data class Success(
        val category: CategoryUIModel,
        val notes: List<BaseNoteUIModel>,
        val isSelectable: Boolean = false,
    ) : AllNotesState

}
