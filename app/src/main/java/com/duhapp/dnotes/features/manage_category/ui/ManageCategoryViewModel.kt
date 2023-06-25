package com.duhapp.dnotes.features.manage_category.ui

import android.view.View
import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.features.add_or_update_category.domain.DeleteCategory
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.base.ui.FragmentViewModel
import com.duhapp.dnotes.features.manage_category.domain.GetCategories
import com.duhapp.dnotes.features.manage_category.domain.InsertCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageCategoryViewModel @Inject constructor(
    private val getCategories: GetCategories,
    private val deleteCategory: DeleteCategory,
    private val insertCategory: InsertCategory,
) : FragmentViewModel<ManageCategoryUIEvent, ManageCategoryUIState>() {
    init {
        setEvent(ManageCategoryUIEvent.Loading)
        setSuccessState(ManageCategoryUIState(emptyList()))
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val list = getCategories.invoke()
            setSuccessState(ManageCategoryUIState(list))
        }
    }

    fun handleCategorySelect(category: CategoryUIModel, position: Int) {
        setEvent(ManageCategoryUIEvent.OnCategorySelected(category.copy()))
    }

    fun onAddCategoryClick() {
        setEvent(ManageCategoryUIEvent.NavigateAddCategory)
    }

    fun handleDeleteCategory(categoryUIModel: CategoryUIModel) {
        viewModelScope.launch {
            try {
                deleteCategory.invoke(categoryUIModel)
                setEvent(
                    ManageCategoryUIEvent.OnCategoryDeleted(categoryUIModel)
                )
                loadCategories()
            } catch (exception: Exception) {
                // TODO: 2021-09-19 handle errors
                setSuccessState(
                    withStateValue {
                        it.apply {
                            it.errorMessage = exception.message ?: ""
                        }
                    })
            }
        }
    }

    fun onCategoryUpserted() {
        loadCategories()
    }

    fun onUndoDelete(categoryUIModel: CategoryUIModel) {
        viewModelScope.launch {
            insertCategory.invoke(categoryUIModel)
            loadCategories()
        }
    }

}

data class ManageCategoryUIState(
    var categoryList: List<CategoryUIModel>,
    var errorMessage: String? = "",
) : FragmentUIState

sealed interface ManageCategoryUIEvent : FragmentUIEvent {
    object NavigateAddCategory : ManageCategoryUIEvent
    data class OnCategorySelected(
        val category: CategoryUIModel,
    ) : ManageCategoryUIEvent
    data class OnCategoryDeleted(
        val category: CategoryUIModel,
    ) : ManageCategoryUIEvent

    object Loading : ManageCategoryUIEvent
    data class ShowItemMenu(
        val categoryUIModel: CategoryUIModel,
    ) : ManageCategoryUIEvent
}
