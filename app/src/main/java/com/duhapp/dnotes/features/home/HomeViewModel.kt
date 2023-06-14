package com.duhapp.dnotes.features.home

import androidx.lifecycle.viewModelScope
import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.domain.FetchHomeData
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.base.ui.FragmentViewModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BasicNoteUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.HomeCategoryUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchHomeData: FetchHomeData
) : FragmentViewModel<HomeUIEvent, HomeUIState>() {

    init {
        setState(HomeUIState(emptyList()))
    }
    fun loadCategories() {
        viewModelScope.launch {
            fetchHomeData.invoke().let {
                if (it.isNotEmpty()) {
                    mutableUIState.emit(
                        HomeUIState(
                            categories = it,
                            errorMessage = "",
                        )
                    )
                } else {
                    mutableUIState.emit(
                        HomeUIState(
                            emptyList(),
                            errorMessage = "No notes found",
                        )
                    )
                }

            }
        }
    }

}

data class HomeUIState(
    val categories: List<HomeCategoryUIModel>,
    val errorMessage: String = "",
) : FragmentUIState

sealed interface HomeUIEvent : FragmentUIEvent {
    object Idle : HomeUIEvent
    object OnCreateNoteClicked : HomeUIEvent
}
