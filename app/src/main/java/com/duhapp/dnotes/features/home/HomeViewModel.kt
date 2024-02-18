package com.duhapp.dnotes.features.home

import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.domain.FetchHomeData
import com.duhapp.dnotes.features.base.domain.CustomException
import com.duhapp.dnotes.features.base.domain.CustomExceptionCode
import com.duhapp.dnotes.features.base.domain.CustomExceptionData
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.base.ui.FragmentViewModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.HomeCategoryUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchHomeData: FetchHomeData
) : FragmentViewModel<HomeUIEvent, HomeUIState>() {

    init {
        setState(
            HomeUIState.Success(
                mutableListOf()
            )
        )
    }

    fun loadCategories() {
        run {
            fetchHomeData.invoke().let { categories ->
                if (categories.isEmpty()) {
                    setState(
                        HomeUIState.Error(
                            CustomException.ThereIsNoSuitableVariableException(
                                CustomExceptionData(
                                    title = R.string.Data_Not_Found,
                                    message = R.string.Note_Data_Not_Found,
                                    code = CustomExceptionCode.THERE_IS_NO_SUITABLE_VARIABLE_EXCEPTION.code,
                                )
                            )
                        )
                    )
                } else {
                    setState(
                        HomeUIState.Success(
                            categories as MutableList<HomeCategoryUIModel>
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

sealed interface HomeUIState : FragmentUIState {
    data class Success(
        var categories: MutableList<HomeCategoryUIModel> = mutableListOf(),
    ) : HomeUIState

    data class Error(
        var customException: CustomException
    ) : HomeUIState

    fun isSuccess() = this is Success

    fun isError() = this is Error

    fun getException() = if (isError()) (this as Error).customException else null

    fun getSuccessCategories() = if (isSuccess()) (this as Success).categories else null
}

sealed interface HomeUIEvent : FragmentUIEvent {
    data class OnNoteClicked(val noteUIModel: BaseNoteUIModel) : HomeUIEvent
    data class OnViewAllClicked(val homeCategoryUIModel: HomeCategoryUIModel) : HomeUIEvent
}
