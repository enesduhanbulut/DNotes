package com.duhapp.dnotes.features.select_category.ui

import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.BottomSheetEvent
import com.duhapp.dnotes.features.base.ui.BottomSheetState
import com.duhapp.dnotes.features.base.ui.BottomSheetViewModel
import com.duhapp.dnotes.features.manage_category.domain.GetCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCategoryViewModel @Inject constructor(
    private val getCategories: GetCategories,
) :
    BottomSheetViewModel<SelectCategoryUIEvent, SelectCategoryUIState>() {
    fun initState() {
        viewModelScope.launch {
            setState(SelectCategoryUIState(getCategories.invoke()))
        }
    }

    fun handleCategorySelect(category: CategoryUIModel) {
        setEvent(SelectCategoryUIEvent.OnCategorySelected(category))
    }
}

sealed interface SelectCategoryUIEvent : BottomSheetEvent {
    object Idle : SelectCategoryUIEvent
    data class OnCategorySelected(val category: CategoryUIModel) : SelectCategoryUIEvent
}

data class SelectCategoryUIState(
    val categories: List<CategoryUIModel>,
) : BottomSheetState
