package com.duhapp.dnotes.features.home

import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.base.ui.BaseViewModel
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.home.home_screen_category.ui.BasicNoteUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.HomeCategoryUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeUIEvent, HomeUIState>() {
    init {
        mutableUIState.value = HomeUIState(
            listOf(
                HomeCategoryUIModel(
                    1,
                    "Title Of Category",
                    listOf(
                        BasicNoteUIModel(
                            1,
                            false,
                            false,
                            false,
                            category = CategoryUIModel(1, "Cat", "Emoji", "Desc"),
                            "Title Of Note",
                            "Test",
                            color = R.color.primary_color,
                        ),
                        BasicNoteUIModel(
                            2,
                            false,
                            false,
                            false,
                            category = CategoryUIModel(1, "Cat", "Emoji", "Desc"),
                            "Title Of Note 1 ",
                            "Test 1",
                            color = R.color.primary_color,
                        ),
                        BasicNoteUIModel(
                            1,
                            false,
                            false,
                            false,
                            category = CategoryUIModel(1, "Cat", "Emoji", "Desc"),
                            "Title Of Note 2",
                            "Test 2",
                            color = R.color.primary_color,
                        ),
                    ),
                ),
                HomeCategoryUIModel(
                    1,
                    "Title Of Category",
                    listOf(
                        BasicNoteUIModel(
                            1,
                            false,
                            false,
                            false,
                            category = CategoryUIModel(1, "Cat", "Emoji", "Desc"),
                            "Title Of Note",
                            "Test",
                            color = R.color.primary_color,
                        ),
                        BasicNoteUIModel(
                            2,
                            false,
                            false,
                            false,
                            category = CategoryUIModel(1, "Cat", "Emoji", "Desc"),
                            "Title Of Note 1 ",
                            "Test 1",
                            color = R.color.primary_color,
                        ),
                        BasicNoteUIModel(
                            1,

                            false,
                            false,
                            false,
                            category = CategoryUIModel(1, "Cat", "Emoji", "Desc"),
                            "Title Of Note 2",
                            "Test 2",
                            color = R.color.primary_color,
                        ),
                    ),
                ),
                HomeCategoryUIModel(
                    1,
                    "Title Of Category",
                    listOf(
                        BasicNoteUIModel(
                            1,
                            false,
                            false,
                            false,
                            category = CategoryUIModel(1, "Cat", "Emoji", "Desc"),
                            "Title Of Note",
                            "Test",
                            color = R.color.primary_color,
                        ),
                        BasicNoteUIModel(
                            2,
                            false,
                            false,
                            false,
                            category = CategoryUIModel(1, "Cat", "Emoji", "Desc"),
                            "Title Of Note 1 ",
                            "Test 1",
                            color = R.color.primary_color,
                        ),
                        BasicNoteUIModel(
                            1,
                            isPinned = false,
                            isCompleted = false,
                            isCompletable = false,
                            category = CategoryUIModel(1, "Cat", "Emoji", "Desc"),
                            title = "Title Of Note 2",
                            body = "Test 2",
                            color = R.color.primary_color,
                        ),
                    ),
                ),
            ),
        )
    }
}

data class HomeUIState(
    val categories: List<HomeCategoryUIModel>,
) : FragmentUIState

sealed interface HomeUIEvent : FragmentUIEvent {
    object OnCreateNoteClicked : HomeUIEvent
}
