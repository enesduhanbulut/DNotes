package com.duhapp.dnotes.features.select_category.ui

import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.BottomSheetEvent
import com.duhapp.dnotes.features.base.ui.BottomSheetState
import com.duhapp.dnotes.features.base.ui.BottomSheetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectCategoryViewModel @Inject constructor() :
    BottomSheetViewModel<SelectCategoryUIEvent, SelectCategoryUIState>() {
    fun handleCategorySelect(category: CategoryUIModel) {
        setEvent(SelectCategoryUIEvent.OnCategorySelected(category))
    }
}

sealed interface SelectCategoryUIEvent : BottomSheetEvent {
    object Idle : SelectCategoryUIEvent
    data class OnCategorySelected(val category: CategoryUIModel) : SelectCategoryUIEvent
}

data class SelectCategoryUIState(
    val selectedCategory: CategoryUIModel = CategoryUIModel(
        id = -1,
        name = "",
        emoji = "",
        description = "",
    ),
) : BottomSheetState
