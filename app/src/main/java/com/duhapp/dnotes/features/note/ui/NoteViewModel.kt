package com.duhapp.dnotes.features.note.ui

import androidx.lifecycle.viewModelScope
import com.ahk.annotation.GenerateSealedGetters
import com.duhapp.dnotes.NoteColor
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.add_or_update_category.ui.ColorItemUIModel
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.asCustomException
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.base.ui.FragmentViewModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BasicNoteUIModel
import com.duhapp.dnotes.features.note.domain.GetDefaultCategory
import com.duhapp.dnotes.features.note.domain.UpsertNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val upsertNote: UpsertNote,
    private val getDefaultCategory: GetDefaultCategory,
) : FragmentViewModel<NoteUIEvent, NoteUIState>() {
    val TAG = "NoteViewModel"

    init {
        setSuccessState(
            NoteUIState.Success(
                SuccessStateData(
                    baseNoteUIModel = getDefaultNote(),
                    editableMode = true,
                )
            )
        )
    }

    fun save() {
        NoteUIStateFunctions.getSuccessStateData(uiState.value)?.let {
            viewModelScope.launch {
                try {
                    val baseNoteUIModel = upsertNote.invoke(it.baseNoteUIModel)
                    setSuccessState(
                        NoteUIState.Success(
                            SuccessStateData(
                                baseNoteUIModel = baseNoteUIModel,
                                editableMode = true,
                            )
                        )
                    )
                } catch (e: Exception) {
                    e.asCustomException(
                        message = R.string.Note_Cannot_Be_Saved
                    )
                }
            }
        } ?: { uiState }
    }

    fun onCategorySelected(category: CategoryUIModel) {
        NoteUIStateFunctions.getSuccessStateData(uiState.value)?.let { noteModel ->
            noteModel.baseNoteUIModel.category = category
            viewModelScope.launch {
                try {
                    val note = upsertNote.invoke(noteModel.baseNoteUIModel)
                    setSuccessState(
                        NoteUIState.Success(
                            SuccessStateData(
                                baseNoteUIModel = note,
                                editableMode = true,
                            )
                        )
                    )
                } catch (e: Exception) {
                    e.asCustomException(
                        message = R.string.Note_Cannot_Be_Saved
                    )
                }
            }
        } ?: { uiState }
    }

    fun initState(args: NoteFragmentArgs?) {
        if (args?.NoteItem == null) {
            viewModelScope.launch {
                setSuccessState(
                    NoteUIState.Success(
                        SuccessStateData(
                            baseNoteUIModel = getDefaultNote(),
                            editableMode = true,
                        )
                    )
                )
            }
        } else {
            setSuccessState(
                NoteUIState.Success(
                    SuccessStateData(
                        baseNoteUIModel = args.NoteItem,
                        editableMode = true,
                    )
                )
            )
        }
    }

    private fun getDefaultNote(): BaseNoteUIModel {
        return BasicNoteUIModel(
            id = -1,
            isPinned = false,
            isCompleted = false,
            isCompletable = false,
            title = "",
            body = "",
            category = CategoryUIModel(
                id = -1,
                name = "",
                emoji = "",
                description = "",
                color = ColorItemUIModel(
                    color = NoteColor.BLUE,
                )
            ),
            color = -1,
        )
    }
}

@GenerateSealedGetters
sealed interface NoteUIState : FragmentUIState {
    data class Error(val stateData: ErrorStateData) : NoteUIState
    object Loading : NoteUIState
    data class Success(
        val stateData: SuccessStateData
    ) : NoteUIState
}

@GenerateSealedGetters
sealed interface NoteUIEvent : FragmentUIEvent {
    object NavigateSelectCategory : NoteUIEvent
}

data class ErrorStateData(
    val error: CustomException?, val string: String?
) : StateData

data class SuccessStateData(
    var baseNoteUIModel: BaseNoteUIModel = BasicNoteUIModel(
        id = -1,
        isPinned = false,
        isCompleted = false,
        isCompletable = false,
        title = "",
        body = "",
        category = CategoryUIModel(
            id = -1,
            name = "",
            emoji = "",
            description = "",
            color = ColorItemUIModel(
                color = NoteColor.BLUE,
            )
        ),
        color = -1,
    ),

    var editableMode: Boolean = true,
    var error: CustomException? = null,
) : StateData

interface StateData