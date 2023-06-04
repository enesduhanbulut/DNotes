package com.duhapp.dnotes.features.manage_category.ui

import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.features.add_or_update_category.domain.DeleteCategory
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.BaseViewModel
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.manage_category.domain.GetCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageCategoryViewModel @Inject constructor(
    private val getCategories: GetCategories,
    private val deleteCategory: DeleteCategory,
) : BaseViewModel<ManageCategoryUIEvent, ManageCategoryUIState>() {
    init {
        setEvent(ManageCategoryUIEvent.Loading)
        mutableUIState.value = ManageCategoryUIState(emptyList())
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getCategories.invoke()
                .collect {
                    mutableUIState.emit(ManageCategoryUIState(it))
                }
        }
    }

    fun handleCategorySelect(category: CategoryUIModel, position: Int) {
        setEvent(ManageCategoryUIEvent.OnCategorySelected(category))
    }

    fun onAddCategoryClick() {
        setEvent(ManageCategoryUIEvent.NavigateAddCategory)
    }

    fun handleDeleteCategory(categoryUIModel: CategoryUIModel) {
        viewModelScope.launch {
            deleteCategory.invoke(categoryUIModel)
            loadCategories()
        }
    }

    fun onCategoryItemSelected(categoryUIModel: CategoryUIModel) {
        setEvent(ManageCategoryUIEvent.ShowItemMenu(categoryUIModel))
    }
}

data class ManageCategoryUIState(
    val categoryList: List<CategoryUIModel>,
) : FragmentUIState

sealed interface ManageCategoryUIEvent : FragmentUIEvent {
    object NavigateAddCategory : ManageCategoryUIEvent
    data class OnCategorySelected(
        val category: CategoryUIModel,
    ) : ManageCategoryUIEvent

    object Loading : ManageCategoryUIEvent
    data class ShowItemMenu(
        val categoryUIModel: CategoryUIModel,
    ) : ManageCategoryUIEvent
}
