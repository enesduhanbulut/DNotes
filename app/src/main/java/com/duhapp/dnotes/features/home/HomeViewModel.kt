package com.duhapp.dnotes.features.home

import com.duhapp.dnotes.R
import com.duhapp.dnotes.features.base.ui.BaseViewModel
import com.duhapp.dnotes.features.base.ui.FragmentUIEvent
import com.duhapp.dnotes.features.base.ui.FragmentUIState
import com.duhapp.dnotes.features.home.home_screen_category.ui.BasicNoteUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.HomeCategoryUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.NoteTag
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeUIEvent, HomeUIState>() {
    init {
        mutableUIState.value = HomeUIState(
            listOf(
                HomeCategoryUIModel(
                    1, "Title Of Category", listOf(
                        BasicNoteUIModel(
                            1, NoteTag(
                                1,
                                "Title Of Note Category",
                                R.color.secondary_light_color,
                                R.drawable.baseline_lightbulb_24
                            ), false, false, false, "Title Of Note", "Test"
                        ),
                        BasicNoteUIModel(
                            2, NoteTag(
                                1,
                                "Title Of Note Category",
                                R.color.secondary_light_color,
                                R.drawable.baseline_lightbulb_24
                            ), false, false, false, "Title Of Note 1 ", "Test 1"
                        ),
                        BasicNoteUIModel(
                            1, NoteTag(
                                1,
                                "Title Of Note Category",
                                R.color.secondary_light_color,
                                R.drawable.baseline_lightbulb_24
                            ), false, false, false, "Title Of Note 2", "Test 2"
                        )
                    )
                ),
                HomeCategoryUIModel(
                    1, "Title Of Category", listOf(
                        BasicNoteUIModel(
                            1, NoteTag(
                                1,
                                "Title Of Note Category",
                                R.color.secondary_light_color,
                                R.drawable.baseline_lightbulb_24
                            ), false, false, false, "Title Of Note", "Test"
                        ),
                        BasicNoteUIModel(
                            2, NoteTag(
                                1,
                                "Title Of Note Category",
                                R.color.secondary_light_color,
                                R.drawable.baseline_lightbulb_24
                            ), false, false, false, "Title Of Note 1 ", "Test 1"
                        ),
                        BasicNoteUIModel(
                            1, NoteTag(
                                1,
                                "Title Of Note Category",
                                R.color.secondary_light_color,
                                R.drawable.baseline_lightbulb_24
                            ), false, false, false, "Title Of Note 2", "Test 2"
                        )
                    )
                ),
                HomeCategoryUIModel(
                    1, "Title Of Category", listOf(
                        BasicNoteUIModel(
                            1, NoteTag(
                                1,
                                "Title Of Note Category",
                                R.color.secondary_light_color,
                                R.drawable.baseline_lightbulb_24
                            ), false, false, false, "Title Of Note", "Test"
                        ),
                        BasicNoteUIModel(
                            2, NoteTag(
                                1,
                                "Title Of Note Category",
                                R.color.secondary_light_color,
                                R.drawable.baseline_lightbulb_24
                            ), false, false, false, "Title Of Note 1 ", "Test 1"
                        ),
                        BasicNoteUIModel(
                            1, NoteTag(
                                1,
                                "Title Of Note Category",
                                R.color.secondary_light_color,
                                R.drawable.baseline_lightbulb_24
                            ), false, false, false, "Title Of Note 2", "Test 2"
                        )
                    )
                )
            )
        )
    }
}

data class HomeUIState(
    val categories: List<HomeCategoryUIModel>
) : FragmentUIState

sealed interface HomeUIEvent : FragmentUIEvent {
    object OnCreateNoteClicked : HomeUIEvent
}