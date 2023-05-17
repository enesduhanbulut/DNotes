package com.duhapp.dnotes.features.select_category.ui

import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.BaseViewModel
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.select_category.domain.GetCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCategoryViewModel @Inject constructor(
    private val getCategories: GetCategories,
) : BaseViewModel<SelectCategoryUIEvent, SelectCategoryUIState>() {
    init {
        mutableUIEvent.value = SelectCategoryUIEvent.Loading
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getCategories.invoke()
                .collect {
                    mutableUIState.emit(SelectCategoryUIState(it))
                }
        }
    }

    fun handleCategorySelect(category: CategoryUIModel, position: Int) {
        mutableUIEvent.value = SelectCategoryUIEvent.OnCategorySelected(category)
    }

    fun onAddCategoryClick() {
        mutableUIEvent.value = SelectCategoryUIEvent.NavigateAddCategory
    }
}

data class SelectCategoryUIState(
    val categoryList: List<CategoryUIModel>,
) : FragmentUIState

sealed interface SelectCategoryUIEvent : FragmentUIEvent {
    object NavigateAddCategory : SelectCategoryUIEvent
    data class OnCategorySelected(
        val category: CategoryUIModel,
    ) : SelectCategoryUIEvent

    object Loading : SelectCategoryUIEvent
}
