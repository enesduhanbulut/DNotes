package com.duhapp.dnotes.features.manage_category.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.ahk.annotation.GenerateSealedGetters
import com.duhapp.dnotes.features.add_or_update_category.domain.DeleteCategory
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.base.ui.FragmentViewModel
import com.duhapp.dnotes.features.manage_category.domain.GetCategories
import com.duhapp.dnotes.features.manage_category.domain.UndoCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageCategoryViewModel @Inject constructor(
    private val getCategories: GetCategories,
    private val deleteCategory: DeleteCategory,
    private val undoCategory: UndoCategory,
) : FragmentViewModel<ManageCategoryUIEvent, ManageCategoryUIState>() {
    init {
        setEvent(ManageCategoryUIEvent.Loading)
        setSuccessState(ManageCategoryUIState.Success(emptyList()))
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val list = getCategories.invoke()
            setSuccessState(ManageCategoryUIState.Success(list))
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
                        }
                    })
            }
        }
    }

    fun onCategoryUpserted() {
        loadCategories()
    }

    fun onUndoDelete() {
        viewModelScope.launch {
            try {
                undoCategory.invoke()
                loadCategories()
            } catch (exception: Exception) {
                // TODO: 2023-06-25 handle errors
                Log.e("ManageCategoryViewModel", "onUndoDelete: ", exception)
            }
        }
    }

}

@GenerateSealedGetters
sealed interface ManageCategoryUIState : FragmentUIState {
    data class Success(
        var categoryList: List<CategoryUIModel>
    ) : ManageCategoryUIState
}

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
