package com.duhapp.dnotes.features.all_notes.ui

import androidx.lifecycle.viewModelScope
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
            AllNotesState(
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
                    setSuccessState(
                        it.copy(
                            category = notes.first().category, notes = notes
                        )
                    )
                }
            }
        }
    }

    fun onNoteClick(noteUIModel: BaseNoteUIModel) {
        val state = uiState.value ?: return
        if (state.isSelectable) {
            val arrangedNoteList = state.notes.map {
                if (it.id == noteUIModel.id) {
                    it.newCopy().apply {
                        isSelected = !noteUIModel.isSelected
                    }
                } else it
            }
            setSuccessState(
                state.copy(
                    notes = arrangedNoteList
                )
            )
        } else {
            setEvent(
                AllNotesEvent.OnEditNoteEvent(noteUIModel)
            )
        }
    }

    fun deleteSelectedNotes() {
        val state = uiState.value ?: return
        val selectedNotes = state.notes.filter {
            it.isSelected
        }
        viewModelScope.launch {
            deleteNote.invoke(selectedNotes)

            getNotesByCategoryId.invoke(state.category.id).let { notes ->
                uiState.value?.let {
                    setSuccessState(
                        it.copy(category = uiState.value!!.category, notes = notes.ifEmpty {
                            emptyList()
                        })
                    )
                }
            }
            clearSelection()
        }
    }


    fun onDeleteNoteClick(noteItem: BaseNoteUIModel) {
        viewModelScope.launch {
            deleteNote.invoke(listOf(noteItem))
            getNotesByCategoryId.invoke(uiState.value!!.category.id).let { notes ->
                uiState.value?.let {
                    setSuccessState(
                        it.copy(category = uiState.value!!.category, notes = notes.ifEmpty {
                            emptyList()
                        })
                    )
                }
            }
        }
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
            getNotesByCategoryId.invoke(uiState.value!!.category.id).let { notes ->
                uiState.value?.let {
                    setSuccessState(
                        it.copy(category = uiState.value!!.category, notes = notes.ifEmpty {
                            emptyList()
                        })
                    )
                }
            }
        }
    }

    fun enableSelectionModeAndSelectANote(noteUIModel: BaseNoteUIModel) {
        val state = uiState.value ?: return
        setSuccessState(state.copy(isSelectable = true, notes = state.notes.map {
            it.newCopy().apply {
                if (this.id == noteUIModel.id) {
                    isSelected = true
                }
                isSelectable = true
            }
        }))
    }

    fun cancelSelectionMode() {
        uiState.value ?: return
        clearSelection()
    }

    fun moveSelectedNotes() {
        val state = uiState.value ?: return
        val selectedNotes = state.notes.filter {
            it.isSelected
        }
        setEvent(
            AllNotesEvent.OnMoveAnotherCategoryEvent(selectedNotes.toMutableList())
        )
        clearSelection()
    }

    private fun clearSelection() {
        val state = uiState.value ?: return
        setSuccessState(state.copy(isSelectable = false, notes = state.notes.map {
            it.newCopy().apply {
                isSelected = false
                isSelectable = false
            }
        }))
    }
}

sealed interface AllNotesEvent : FragmentUIEvent {
    object Idle : AllNotesEvent
    data class OnEditNoteEvent(val noteUIModel: BaseNoteUIModel) : AllNotesEvent
    data class OnMoveAnotherCategoryEvent(val noteUIModel: MutableList<BaseNoteUIModel>) :
        AllNotesEvent
}

data class AllNotesState(
    val category: CategoryUIModel,
    val notes: List<BaseNoteUIModel>,
    val isSelectable: Boolean = false,
) : FragmentUIState
