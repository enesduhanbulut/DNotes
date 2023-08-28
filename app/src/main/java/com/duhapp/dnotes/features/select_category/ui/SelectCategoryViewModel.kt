package com.duhapp.dnotes.features.select_category.ui

import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.asCustomException
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
) : BottomSheetViewModel<SelectCategoryUIEvent, SelectCategoryUIState>() {
    fun initState(selectedItemModel: CategoryUIModel) {
        setState(
            SelectCategoryUIState.Success(
                selectedItemModel,
                emptyList()
            )
        )
        viewModelScope.launch {
            try {
                val categories = getCategories.invoke()
                setState(
                    SelectCategoryUIState.Success(
                        selectedItemModel,
                        categories
                    )
                )
            } catch (e: Exception) {
                setState(
                    SelectCategoryUIState.Error(
                        e.asCustomException(
                            message = R.string.Categories_Could_Not_Be_Loaded,
                        )
                    )
                )
            }
        }
    }

    fun handleCategorySelect(category: CategoryUIModel) {
        setState(
            withStateValue {
                if (it.isSuccess()) {
                    return@withStateValue SelectCategoryUIState.Success(
                        category,
                        it.getSuccessCategories() ?: emptyList(),
                        it.getSuccessIsExpanded() ?: false
                    )
                }
                it
            }
        )
        setEvent(SelectCategoryUIEvent.OnCategorySelected(category))
    }

    fun onExpand() {
        setState(
            withStateValue {
                if (it.isSuccess()) {
                    return@withStateValue SelectCategoryUIState.Success(
                        it.getSuccessSelectedCategory() ?: CategoryUIModel(),
                        it.getSuccessCategories() ?: emptyList(),
                        true
                    )
                }
                it
            }
        )
    }

    fun onCollapse() {
        setState(
            withStateValue {
                if (it.isSuccess()) {
                    return@withStateValue SelectCategoryUIState.Success(
                        it.getSuccessSelectedCategory() ?: CategoryUIModel(),
                        it.getSuccessCategories() ?: emptyList(),
                        false
                    )
                }
                it
            }
        )
    }
}

sealed interface SelectCategoryUIEvent : BottomSheetEvent {
    object Idle : SelectCategoryUIEvent
    object Dismiss : SelectCategoryUIEvent
    data class OnCategorySelected(val category: CategoryUIModel) : SelectCategoryUIEvent
}

sealed interface SelectCategoryUIState : BottomSheetState {
    data class Success(
        val selectedCategory: CategoryUIModel,
        val categories: List<CategoryUIModel>,
        val isExpanded: Boolean = false,
    ) : SelectCategoryUIState

    data class Error(
        val customException: CustomException,
    ) : SelectCategoryUIState

    fun isSuccess() = this is Success

    fun isError() = this is Error

    fun getSuccessSelectedCategory() = if (isSuccess()) (this as Success).selectedCategory else null

    fun getSuccessCategories() = if (isSuccess()) (this as Success).categories else null

    fun getSuccessIsExpanded() = if (isSuccess()) (this as Success).isExpanded else null

    fun getException() = if (isError()) (this as Error).customException else null
}