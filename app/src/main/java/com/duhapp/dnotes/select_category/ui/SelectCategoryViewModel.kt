package com.duhapp.dnotes.select_category.ui

import com.duhapp.dnotes.base.ui.BaseViewModel
import com.duhapp.dnotes.base.ui.FragmentUIEvent
import com.duhapp.dnotes.base.ui.FragmentUIState

class SelectCategoryViewModel : BaseViewModel<SelectCategoryUIEvent, SelectCategoryUIState>()

interface SelectCategoryUIState : FragmentUIState

sealed interface SelectCategoryUIEvent : FragmentUIEvent {
    object OnCreateNoteClicked : SelectCategoryUIEvent
}