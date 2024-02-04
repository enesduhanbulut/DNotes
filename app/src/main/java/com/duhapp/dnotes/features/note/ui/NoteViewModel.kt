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
    var selectedCategory: CategoryUIModel? = DEFAULT_NOTE_MODEL.category
    var lastUIEvent: NoteUIEvent? = null
    var lastSuccesState: NoteUIState.Success? = null

    override fun setState(state: NoteUIState) {
        Timber.d( "setState: $state")
        if (state is NoteUIState.Success) {
            lastSuccesState = state
        }
        Timber.d( "lastSuccesState: $lastSuccesState")
        super.setState(state)
    }

    override fun setEvent(event: NoteUIEvent) = viewModelScope.launch {
        lastUIEvent = event
        super.setEvent(event)
    }


    private fun save() {
        withStateValue {
            run {
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
                    Timber.e(e)
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
            run {
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
                    Timber.e(e)
                    val errorState = NoteUIState.Error(
                        e.asCustomException(
                            message = R.string.Note_Could_Not_Be_Updated
                        )
                    )
                    setState(
                        errorState
                    )
                    setEvent(NoteUIEvent.ShowWarningDialogBeforeExit(errorState.customException))
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
        setEvent(NoteUIEvent.CollapseBottomSheet)
    }

    fun initStateWithLastSuccessState() {
        lastSuccesState?.let {
            setState(
                NoteUIState.Success(
                    baseNoteUIModel = it.baseNoteUIModel,
                    editableMode = true,
                )
            )
        } ?: initState(null)
    }

    fun initState(args: NoteFragmentArgs?) {
        setState(
            NoteUIState.Idle
        )
        if (args == null || args.NoteItem == null) {
            Timber.d("Insert mode opened")
            run {
                try {
                    val defaultCategory = getDefaultCategory.invoke()
                    val state = uiState.value!!
                    var note = state.getSuccessNote() ?: DEFAULT_NOTE_MODEL.newCopy()
                    note = note.newCopy().apply {
                        category = defaultCategory
                    }
                    selectedCategory = note.category
                    setState(
                        NoteUIState.Success(
                            baseNoteUIModel = note,
                            editableMode = true,
                        )
                    )
                } catch (e: Exception) {
                    Timber.e(e)
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
            Timber.d( "Update mode opened")
            args.NoteItem.let {
                selectedCategory = it.category
                setState(
                    NoteUIState.Success(
                        baseNoteUIModel = it,
                        editableMode = true,
                    )
                )
            }
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

    fun saveAccordingToLastUIEvent() {
        if (lastUIEvent !is NoteUIEvent.BackButtonClicked)
            save()
    }
}

sealed interface NoteUIEvent : FragmentUIEvent {
    object GoToBackStack : NoteUIEvent
    data class ShowWarningDialogBeforeExit(
        val customException: CustomException
    ) : NoteUIEvent

    object BackButtonClicked : NoteUIEvent
    object CollapseBottomSheet : NoteUIEvent
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

    fun getSuccessNote() = if (isSuccess()) (this as Success).baseNoteUIModel else null

    fun setSuccessTitle(title: String) =
        if (isSuccess()) (this as Success).baseNoteUIModel.title = title else Unit

    fun setSuccessBody(body: String) =
        if (isSuccess()) (this as Success).baseNoteUIModel.body = body else Unit

    fun getSuccessEditableMode() = isSuccess() && (this as Success).editableMode
}