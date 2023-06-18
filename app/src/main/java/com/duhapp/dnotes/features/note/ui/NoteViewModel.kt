package com.duhapp.dnotes.features.note.ui

import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.NoteColor
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.add_or_update_category.ui.ColorItemUIModel
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

    init {
        setSuccessState(getDefaultNote())
    }

    fun switchEditMode() {
        withStateValue {
            if (it.editableMode) {
                viewModelScope.launch {
                    upsertNote.invoke(it.baseNoteUIModel)
                }
            }
            it
        }
        setSuccessState(
            withStateValue {
                it.copy(
                    editableMode = !it.editableMode
                )
            })

    }

    fun onCategorySelected(category: CategoryUIModel) {
        setSuccessState(
            withStateValue {
                it.copy(
                    baseNoteUIModel = it.baseNoteUIModel.newCopy().apply {
                        this.category = category
                    }
                )
            }
        )
    }

    fun categorySelectClicked() {
        setEvent(NoteUIEvent.NavigateSelectCategory)
        setEvent(NoteUIEvent.Loading)
    }

    fun initState(args: NoteFragmentArgs?) {
        if (args?.NoteItem == null) {
            viewModelScope.launch {
                getDefaultCategory.invoke().let { defaultCategory ->
                    setSuccessState(
                        withStateValue {
                            it.baseNoteUIModel.category = defaultCategory
                            it.editableMode = true
                            it
                        }
                    )
                }
            }
        } else {
            setSuccessState(
                NoteUIState(
                    baseNoteUIModel = args.NoteItem,
                    editableMode = false,
                )
            )
        }
    }

    private fun getDefaultNote(): NoteUIState {
        return NoteUIState(
            baseNoteUIModel = BasicNoteUIModel(
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
            editableMode = true,
        )
    }
}

sealed interface NoteUIEvent : FragmentUIEvent {
    object NavigateSelectCategory : NoteUIEvent
    object Loading : NoteUIEvent
}

data class NoteUIState(
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
    var editableMode: Boolean,
) : FragmentUIState
