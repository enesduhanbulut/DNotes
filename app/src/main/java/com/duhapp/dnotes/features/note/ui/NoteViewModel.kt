package com.duhapp.dnotes.features.note.ui

import androidx.lifecycle.viewModelScope
import com.ahk.annotation.GenerateSealedGetters
import com.duhapp.dnotes.NoteColor
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.add_or_update_category.ui.ColorItemUIModel
import com.duhapp.dnotes.features.base.domain.CustomException
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
) : FragmentViewModel<NoteUIEvent, NoteUIState, NoteUIStateFunctions>() {
    val TAG = "NoteViewModel"

    init {
        setState(
            NoteUIState.Success(
                SuccessStateData(
                    baseNoteUIModel = getDefaultNote(),
                    editableMode = true,
                )
            )
        )
    }

    fun save() {

    }

    fun onCategorySelected(category: CategoryUIModel) {

    }

    fun initState(args: NoteFragmentArgs?) {
        if (args?.NoteItem == null) {
            viewModelScope.launch {
                setState(
                    NoteUIState.Success(
                        SuccessStateData(
                            baseNoteUIModel = getDefaultNote(),
                            editableMode = true,
                        )
                    )
                )
            }
        } else {
            setState(
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