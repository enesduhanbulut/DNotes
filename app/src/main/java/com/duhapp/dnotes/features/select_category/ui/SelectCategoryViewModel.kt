package com.duhapp.dnotes.features.select_category.ui

import androidx.lifecycle.viewModelScope
import com.ahk.annotation.GenerateSealedGetters
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
        setSuccessState(
            SelectCategoryUIState.Success(
                SuccessStateData(
                    selectedCategory = selectedItemModel,
                    categories = emptyList(),
                    isExpanded = false,
                )
            )
        )
        viewModelScope.launch {
            try {
                val categories = getCategories.invoke()
                setSuccessState(
                    SelectCategoryUIState.Success(
                        SuccessStateData(
                            selectedCategory = selectedItemModel,
                            categories = categories,
                            isExpanded = false,
                        )
                    )
                )
            } catch (e: Exception) {
                setSuccessState(
                    SelectCategoryUIState.Error(
                        e.asCustomException(message = R.string.Categories_Cannot_Be_Loaded)
                    )
                )
            }
        }
    }

    fun handleCategorySelect(category: CategoryUIModel) {
        setSuccessState(
            withStateValue { uiState ->
                SelectCategoryUIStateFunctions.getSuccessStateData(uiState)?.let {
                    SelectCategoryUIState.Success(
                        SuccessStateData(
                            selectedCategory = category,
                            categories = it.categories,
                            isExpanded = it.isExpanded,
                        )
                    )
                } ?: uiState
            }
        )
        setEvent(SelectCategoryUIEvent.OnCategorySelected(category))
    }

    fun onExpand() {
        withStateValue { uiState ->
            val state = SelectCategoryUIStateFunctions.getSuccessStateData(uiState)?.let {
                SelectCategoryUIState.Success(
                    SuccessStateData(
                        selectedCategory = it.selectedCategory,
                        categories = it.categories,
                        isExpanded = true,
                    )
                )
            } ?: uiState
            setSuccessState(state)
            state
        }
    }

    fun onCollapse() {
        withStateValue { uiState ->
            val state = SelectCategoryUIStateFunctions.getSuccessStateData(uiState)?.let {
                SelectCategoryUIState.Success(
                    SuccessStateData(
                        selectedCategory = it.selectedCategory,
                        categories = it.categories,
                        isExpanded = false,
                    )
                )
            } ?: uiState
            setSuccessState(state)
            state
        }
    }

}

sealed interface SelectCategoryUIEvent : BottomSheetEvent {
    object Idle : SelectCategoryUIEvent
    object Dismiss : SelectCategoryUIEvent
    data class OnCategorySelected(val category: CategoryUIModel) : SelectCategoryUIEvent
}

@GenerateSealedGetters
sealed interface SelectCategoryUIState : BottomSheetState {
    data class Success(
        val stateData: SuccessStateData,
    ) : SelectCategoryUIState

    data class Error(
        val errorStateData: CustomException,
    ) : SelectCategoryUIState
    object Loading : SelectCategoryUIState
}

data class SuccessStateData(
    val selectedCategory: CategoryUIModel,
    val categories: List<CategoryUIModel>,
    val isExpanded: Boolean = false,
) : StateData

data class ErrorStateData(
    val customException: CustomException,
) : StateData

interface StateData