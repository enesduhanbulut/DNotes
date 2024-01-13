package com.duhapp.dnotes.features.all_notes.ui

import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.all_notes.domain.DeleteNote
import com.duhapp.dnotes.features.all_notes.domain.GetNotesByCategoryId
import com.duhapp.dnotes.features.all_notes.domain.UpdateNotes
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionCode
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.base.domain.asCustomException
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
        setState(
            AllNotesState.Success(
                category = defaultCategoryModel, notes = emptyList()
            )
        )

        run {
            try {
                val notesById = getNotesByCategoryId.invoke(categoryId)
                if (notesById.isEmpty()) {
                    setState(
                        AllNotesState.Error(
                            CustomException.ThereIsNoSuitableVariableException(
                                CustomExceptionData(
                                    R.string.Error_There_Is_No_Suitable_Variable,
                                    R.string.Notes_Could_Not_Be_Found_By_Category,
                                    CustomExceptionCode.THERE_IS_NO_SUITABLE_VARIABLE_EXCEPTION.code
                                )
                            )
                        )
                    )
                    setEvent(AllNotesEvent.NavigateToHome)
                } else {
                    setState(
                        AllNotesState.Success(
                            category = notesById.first().category, notes = notesById
                        )
                    )
                }
            } catch (e: Exception) {
                setState(
                    AllNotesState.Error(
                        e.asCustomException(
                            message = R.string.Notes_Could_Not_Be_Found_By_Category
                        )
                    )
                )
            }
        }
    }

    fun onNoteClick(noteUIModel: BaseNoteUIModel) {
        val state = uiState.value ?: return
        val isSelectable = state.getSuccessIsSelectable() ?: return
        val notes = state.getSuccessNotes() ?: return
        val category = state.getSuccessCategory() ?: return
        if (isSelectable) {
            val arrangedNoteList = notes.map {
                if (it.id == noteUIModel.id) {
                    it.newCopy().apply {
                        isSelected = !noteUIModel.isSelected
                    }
                } else it
            }
            setState(
                AllNotesState.Success(
                    category = category,
                    notes = arrangedNoteList,
                    isSelectable = true
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
        if (!state.isSuccess()) return
        val selectedNotes = state.getSuccessSelectedNotes()!!
        val category = state.getSuccessCategory()!!
        viewModelScope.launch {
            try {
                deleteNote.invoke(selectedNotes)
                val notes = getNotesByCategoryId.invoke(category.id)
                setState(
                    AllNotesState.Success(
                        category = category,
                        notes = notes,
                        isSelectable = false
                    )
                )
            } catch (e: Exception) {
                setState(
                    AllNotesState.Error(
                        customException = e.asCustomException(
                            message = R.string.Selected_Notes_Could_Not_Be_Deleted
                        )
                    )
                )
            }
            clearSelection()
        }
    }


    fun onDeleteNoteClick(noteItem: BaseNoteUIModel) {
        setState(
            withStateValue { state ->
                var stateVal = state
                viewModelScope.launch {
                    try {
                        deleteNote.invoke(listOf(noteItem))
                        if (!stateVal.isSuccess()) {
                            stateVal = state
                            return@launch
                        }
                        val notes = getNotesByCategoryId.invoke(stateVal.getSuccessCategory()!!.id)
                        stateVal = AllNotesState.Success(
                            category = stateVal.getSuccessCategory()!!,
                            notes = notes
                        )
                    } catch (e: Exception) {
                        stateVal = AllNotesState.Error(
                            customException = e.asCustomException(
                                message = R.string.Note_Could_Not_Be_Updated
                            )
                        )
                    }

                }
                stateVal
            }
        )
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
        noteUIModel.forEach {
            it.category = category
        }
        viewModelScope.launch {
            updateNotes.invoke(noteUIModel)
            val state = uiState.value ?: return@launch
            if (!state.isSuccess()) return@launch
            try {
                val notes = getNotesByCategoryId.invoke(state.getSuccessCategory()!!.id)
                setState(
                    AllNotesState.Success(
                        category = state.getSuccessCategory()!!,
                        notes = notes,
                        isSelectable = false
                    )
                )
                if (notes.isEmpty()) setEvent(AllNotesEvent.NavigateToHome)
            } catch (e: Exception) {
                setState(
                    AllNotesState.Error(
                        customException = e.asCustomException(
                            message = R.string.Note_Could_Not_Be_Updated
                        )
                    )
                )
            }
        }
    }

    fun enableSelectionModeAndSelectANote(noteUIModel: BaseNoteUIModel) {
        val state = uiState.value ?: return
        if (!state.isSuccess()) return

        val notes = state.getSuccessNotes()!!.map { note ->
            note.newCopy().apply {
                isSelectable = true
                if (this.id == noteUIModel.id) {
                    isSelected = true
                }
            }
        }
        setState(
            AllNotesState.Success(
                category = state.getSuccessCategory()!!,
                notes = notes,
                isSelectable = true
            )
        )
    }

    fun cancelSelectionMode() {
        uiState.value ?: return
        clearSelection()
    }

    fun moveSelectedNotes() {
        val state = uiState.value ?: return

        val selectedNotes = state.getSuccessNotes()?.filter {
            it.isSelected
        } ?: return

        setEvent(
            AllNotesEvent.OnMoveAnotherCategoryEvent(selectedNotes.toMutableList())
        )
        clearSelection()
    }

    private fun clearSelection() {
        val state = uiState.value ?: return
        if (!state.isSuccess()) return
        val noteList = state.getSuccessNotes()!!.map {
            it.newCopy().apply {
                isSelectable = false
                isSelected = false
            }
        }
        setState(
            AllNotesState.Success(
                category = state.getSuccessCategory()!!,
                notes = noteList,
                isSelectable = false
            )
        )
    }
}

sealed interface AllNotesEvent : FragmentUIEvent {
    object Idle : AllNotesEvent
    data class OnEditNoteEvent(val noteUIModel: BaseNoteUIModel) : AllNotesEvent
    data class OnMoveAnotherCategoryEvent(val noteUIModel: MutableList<BaseNoteUIModel>) :
        AllNotesEvent

    object NavigateToHome : AllNotesEvent
}

sealed interface AllNotesState : FragmentUIState {
    data class Success(
        val category: CategoryUIModel,
        val notes: List<BaseNoteUIModel>,
        val isSelectable: Boolean = false,
    ) : AllNotesState

    data class Error(
        val customException: CustomException
    ) : AllNotesState

    fun isSuccess() = this is Success

    fun isError() = this is Error

    fun getSuccessCategory() = (this as? Success)?.category

    fun getSuccessNotes() = (this as? Success)?.notes

    fun getSuccessIsSelectable() = (this as? Success)?.isSelectable

    fun getSuccessSelectedNotes() = (this as? Success)?.notes?.filter {
        it.isSelected
    }

    fun getSuccessSelectedNote() = (this as? Success)?.notes?.firstOrNull {
        it.isSelected
    }

    fun getSuccessUnSelectedNotes() = (this as? Success)?.notes?.filter {
        !it.isSelected
    }

    fun getSuccessUnSelectedNote() = (this as? Success)?.notes?.firstOrNull {
        !it.isSelected
    }

    fun getException() = (this as? Error)?.customException
}