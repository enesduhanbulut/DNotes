package com.duhapp.dnotes.features.select_category.ui

import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.features.add_or_update_category.domain.DeleteCategory
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
    private val deleteCategory: DeleteCategory
) : BaseViewModel<SelectCategoryUIEvent, SelectCategoryUIState>() {
    init {
        setEvent(SelectCategoryUIEvent.Loading)
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
        setEvent(SelectCategoryUIEvent.OnCategorySelected(category))
    }

    fun onAddCategoryClick() {
        setEvent(SelectCategoryUIEvent.NavigateAddCategory)
    }

    fun handleDeleteCategory(categoryUIModel: CategoryUIModel) {
        viewModelScope.launch {
            deleteCategory.invoke(categoryUIModel)
            loadCategories()
        }
    }
}

data class SelectCategoryUIState(
    val categoryList: List<CategoryUIModel>
) : FragmentUIState

sealed interface SelectCategoryUIEvent : FragmentUIEvent {
    object NavigateAddCategory : SelectCategoryUIEvent

    data class OnCategorySelected(val categoryUIModel: CategoryUIModel) : SelectCategoryUIEvent
    object Loading : SelectCategoryUIEvent
    data class ShowItemMenu(
        val categoryUIModel: CategoryUIModel
    ) : SelectCategoryUIEvent
}