package com.duhapp.dnotes.features.manage_category.ui

import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.domain.DeleteCategory
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.asCustomException
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.base.ui.FragmentViewModel
import com.duhapp.dnotes.features.manage_category.domain.GetCategories
import com.duhapp.dnotes.features.manage_category.domain.UndoCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ManageCategoryViewModel @Inject constructor(
    private val getCategories: GetCategories,
    private val deleteCategory: DeleteCategory,
    private val undoCategory: UndoCategory,
) : FragmentViewModel<ManageCategoryUIEvent, ManageCategoryUIState>() {
    init {
        setEvent(ManageCategoryUIEvent.Loading)
        setState(ManageCategoryUIState.Success(emptyList()))
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val list = getCategories.invoke()
                setState(ManageCategoryUIState.Success(list))
            } catch (exception: Exception) {
                Timber.e("ManageCategoryViewModel", "loadCategories: ", exception)
                setStateAndRunMethodAfterDelay(
                    ManageCategoryUIState.Error(
                        exception.asCustomException(
                            message = R.string.Error_While_Fetching_Category,
                        )
                    ),
                    1000
                ) {
                    loadCategories()
                }
            }
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
                Timber.e("ManageCategoryViewModel", "handleDeleteCategory: ", exception)
                setStateAndRunMethodAfterDelay(
                    ManageCategoryUIState.Error(
                        exception.asCustomException(
                            message = R.string.Error_While_Deleting_Category,
                        )
                    ),
                    1000
                ) {
                    loadCategories()
                }
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
                Timber.e("ManageCategoryViewModel", "onUndoDelete: ", exception)
                setStateAndRunMethodAfterDelay(
                    ManageCategoryUIState.Error(
                        exception.asCustomException(
                            message = R.string.Error_While_Undoing_Category_Deletion,
                        )
                    ),
                    1000
                ) {
                    loadCategories()
                }
            }
        }
    }

}

sealed interface ManageCategoryUIState: FragmentUIState {
    data class Success(
        val categoryList: List<CategoryUIModel> = emptyList(),
    ) : ManageCategoryUIState

    data class Error(
        val customException: CustomException,
    ) : ManageCategoryUIState

    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    fun getSuccessCategoryList(): List<CategoryUIModel>? = if (this is Success) categoryList else null
    fun getErrorCustomException(): CustomException? = if (this is Error) customException else null
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
