package com.duhapp.dnotes.features.note.ui

import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.asCustomException
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.base.ui.FragmentViewModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.DEFAULT_NOTE_MODEL
import com.duhapp.dnotes.features.note.domain.GetDefaultCategory
import com.duhapp.dnotes.features.note.domain.UpsertNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val upsertNote: UpsertNote,
    private val getDefaultCategory: GetDefaultCategory,
) : FragmentViewModel<NoteUIEvent, NoteUIState>() {
    private val TAG = "NoteViewModel"
    var selectedCategory: CategoryUIModel? = DEFAULT_NOTE_MODEL.category

    fun save() {
        withStateValue {
            viewModelScope.launch {
                try {
                    val noteModel = upsertNote.invoke(
                        it.getSuccessNote()!!
                    )
                    setState(
                        NoteUIState.Success(
                            baseNoteUIModel = noteModel,
                            editableMode = false,
                        )
                    )
                } catch (e: Exception) {
                    Timber.e(TAG, e)
                    setState(
                        NoteUIState.Error(
                            e.asCustomException(
                                message = R.string.Note_Could_Not_Be_Updated
                            )
                        ),
                    )
                }
            }
            it
        }
    }

    fun setBackClicked() {
        setEvent(NoteUIEvent.BackButtonClicked)
    }

    fun saveAndGoBackStack() {
        withStateValue {
            viewModelScope.launch {
                try {
                    val noteModel = upsertNote.invoke(
                        it.getSuccessNote()!!
                    )
                    setState(
                        NoteUIState.Success(
                            baseNoteUIModel = noteModel,
                            editableMode = false,
                        )
                    )
                    setEvent(NoteUIEvent.GoToBackStack)
                } catch (e: Exception) {
                    Timber.e(TAG, e)
                    setState(
                        NoteUIState.Error(
                            e.asCustomException(
                                message = R.string.Note_Could_Not_Be_Updated
                            )
                        ),
                    )
                    setEvent(NoteUIEvent.ShowWarningDialogBeforeExit)
                }
            }
            it
        }
    }

    fun onCategorySelected(category: CategoryUIModel) {
        selectedCategory = category
        setState(
            withStateValue { state ->
                return@withStateValue if (state.isSuccess()) {
                    val noteModel = state.getSuccessNote()!!
                    noteModel.category = category
                    NoteUIState.Success(
                        baseNoteUIModel = noteModel,
                        editableMode = true,
                    )
                } else state
            }
        )
    }

    fun initState(args: NoteFragmentArgs?) {
        setState(
            NoteUIState.Idle
        )
        if (args == null || args.NoteItem == null) {
            Timber.d(TAG, "Insert mode opened")
            viewModelScope.launch {
                try {
                    val defaultCategory = getDefaultCategory.invoke()
                    val state = uiState.value!!
                    val note = state.getSuccessNote()?.apply { category = defaultCategory }
                        ?: DEFAULT_NOTE_MODEL.newCopy()
                    selectedCategory = note.category
                    setState(
                        NoteUIState.Success(
                            baseNoteUIModel = note,
                            editableMode = true,
                        )
                    )
                } catch (e: Exception) {
                    Timber.e(TAG, e)
                    setState(
                        NoteUIState.Error(
                            e.asCustomException(
                                message = R.string.Note_Could_Not_Be_Updated
                            )
                        ),
                    )
                }
            }
        } else {
            Timber.d(TAG, "Update mode opened")
            selectedCategory = args.NoteItem!!.category
            setState(
                NoteUIState.Success(
                    baseNoteUIModel = args.NoteItem!!,
                    editableMode = true,
                )
            )
        }
    }

    fun setEditable(editable: Boolean) {
        setState(
            withStateValue {
                if (it.isSuccess()) {
                    NoteUIState.Success(
                        it.getSuccessNote()!!,
                        editable
                    )
                } else it
            }
        )
    }
}

sealed interface NoteUIEvent : FragmentUIEvent {
    object Idle : NoteUIEvent
    object NavigateSelectCategory : NoteUIEvent
    object Loading : NoteUIEvent
    object GoToBackStack : NoteUIEvent
    object ShowWarningDialogBeforeExit : NoteUIEvent
    object BackButtonClicked : NoteUIEvent
}

sealed interface NoteUIState : FragmentUIState {
    object Idle : NoteUIState

    data class Success(
        var baseNoteUIModel: BaseNoteUIModel = DEFAULT_NOTE_MODEL.newCopy(),
        var editableMode: Boolean = true,
    ) : NoteUIState

    data class Error(
        var customException: CustomException
    ) : NoteUIState

    fun isSuccess() = this is Success

    fun isError() = this is Error

    fun getException() = if (isError()) (this as Error).customException else null

    fun getSuccessNote() = if (isSuccess()) (this as Success).baseNoteUIModel else null

    fun setSuccessTitle(title: String) =
        if (isSuccess()) (this as Success).baseNoteUIModel.title = title else Unit

    fun setSuccessBody(body: String) =
        if (isSuccess()) (this as Success).baseNoteUIModel.body = body else Unit

    fun getSuccessEditableMode() = if (isSuccess()) (this as Success).editableMode else false
}