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
) : BottomSheetViewModel<SelectCategoryUIEvent, SelectCategoryUIState>() {
    fun initState(selectedItemModel: CategoryUIModel) {
        setSuccessState(SelectCategoryUIState(selectedItemModel, emptyList()))
        viewModelScope.launch {
            val categories = getCategories.invoke()
            setSuccessState(
                withStateValue {
                    val state = it.copy(
                        categories = categories
                    )
                    state
                }
            )
        }
    }

    fun handleCategorySelect(category: CategoryUIModel) {
        setSuccessState(
            withStateValue {
                it.copy(
                    selectedCategory = category
                )
            }
        )
        setEvent(SelectCategoryUIEvent.OnCategorySelected(category))
    }

    fun onExpand() {
        setSuccessState(
            withStateValue {
                it.copy(
                    isExpanded = true
                )
            }
        )
    }

    fun onCollapse() {
        setSuccessState(
            withStateValue {
                it.copy(
                    isExpanded = false
                )
            }
        )
    }

}

sealed interface SelectCategoryUIEvent : BottomSheetEvent {
    object Idle : SelectCategoryUIEvent
    object Dismiss : SelectCategoryUIEvent
    data class OnCategorySelected(val category: CategoryUIModel) : SelectCategoryUIEvent
}

data class SelectCategoryUIState(
    var selectedCategory: CategoryUIModel? = null,
    val categories: List<CategoryUIModel>,
    var isExpanded: Boolean = false,
) : BottomSheetState
