package com.duhapp.dnotes.features.select_category.ui

import com.duhapp.dnotes.features.base.ui.BaseViewModel
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState

class SelectCategoryViewModel : BaseViewModel<SelectCategoryUIEvent, SelectCategoryUIState>() {
    fun onAddCategoryClick() {
        mutableUIEvent.value = SelectCategoryUIEvent.NavigateAddCategory
    }
}

interface SelectCategoryUIState : FragmentUIState

sealed interface SelectCategoryUIEvent : FragmentUIEvent {
    object NavigateAddCategory : SelectCategoryUIEvent

    object OnCreateNoteClicked : SelectCategoryUIEvent
}