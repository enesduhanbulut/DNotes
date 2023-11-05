package com.duhapp.dnotes.features.home

import androidx.lifecycle.viewModelScope
import com.ahk.annotation.GenerateSealedGetters
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.domain.FetchHomeData
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryBottomSheetUIState
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.base.ui.FragmentViewModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.HomeCategoryUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchHomeData: FetchHomeData
) : FragmentViewModel<HomeUIEvent, HomeUIState, HomeUIStateFunctions>() {

    init {
        setState(HomeUIState.Success(emptyList()))
    }

    fun loadCategories() {
        viewModelScope.launch {
            fetchHomeData.invoke().let {
                if (it.isNotEmpty()) {
                    setState(
                        HomeUIState.Success(
                            it
                        )
                    )

                } else {
                    setState(
                        HomeUIState.Error(
                            CustomException.ThereIsNoSuitableVariableException(
                                CustomExceptionData(
                                    R.string.Data_Not_Found,
                                    R.string.Categories_Cannot_Be_Loaded,
                                    -1,
                                )
                            )
                        )
                    )
                }

            }
        }
    }

    fun onNoteClick(noteUIModel: BaseNoteUIModel) {
        setEvent(HomeUIEvent.OnNoteClicked(noteUIModel))
    }

    fun onCategoryViewAllClicked(homeCategoryUIModel: HomeCategoryUIModel) {
        setEvent(HomeUIEvent.OnViewAllClicked(homeCategoryUIModel))
    }

}

@GenerateSealedGetters
sealed interface HomeUIState : FragmentUIState {
    data class Success(
        val categories: List<HomeCategoryUIModel>
    ) : HomeUIState
    data class Error(
        val customException: CustomException
    ) : HomeUIState
}

sealed interface HomeUIEvent : FragmentUIEvent {
    object Idle : HomeUIEvent
    object OnCreateNoteClicked : HomeUIEvent
    data class OnNoteClicked(val noteUIModel: BaseNoteUIModel) : HomeUIEvent
    data class OnViewAllClicked(val homeCategoryUIModel: HomeCategoryUIModel) : HomeUIEvent
}
