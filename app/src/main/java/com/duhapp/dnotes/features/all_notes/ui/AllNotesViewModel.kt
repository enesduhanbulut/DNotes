package com.duhapp.dnotes.features.all_notes.ui

import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.all_notes.domain.GetNotesByCategoryId
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
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
                setFailureState(
                    CustomException.ThereIsNoSuitableVariableException(
                        CustomExceptionData(
                            R.string.Data_Not_Found,
                            R.string.Note_Data_Not_Found,
                            -1,
                        )
                    )
                )
            } else {
                uiState.value?.let {
                    setSuccessState(
                        it.copy(
                            category = notes.first().category,
                            notes = notes
                        )
                    )
                }
            }
        }
    }

    fun onNoteClick(noteUIModel: BaseNoteUIModel) {
        setEvent(
            AllNotesEvent.OnNoteClicked(noteUIModel)
        )
    }
}

sealed interface AllNotesEvent : FragmentUIEvent {
    object Idle : AllNotesEvent
    data class OnNoteClicked(val noteUIModel: BaseNoteUIModel) : AllNotesEvent
}

data class AllNotesState(
    val category: CategoryUIModel,
    val notes: List<BaseNoteUIModel>
) : FragmentUIState
