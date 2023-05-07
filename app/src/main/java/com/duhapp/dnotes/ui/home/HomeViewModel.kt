package com.duhapp.dnotes.ui.home

import com.duhapp.dnotes.R
import com.duhapp.dnotes.base.ui.BaseViewModel
import com.duhapp.dnotes.home_category.ui.BasicNoteUIModel
import com.duhapp.dnotes.home_category.ui.HomeCategoryUIModel
import com.duhapp.dnotes.home_category.ui.NoteTag
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
                                R.color.yellow_higher_alpha,
                                R.drawable.baseline_lightbulb_24
                            ), false, false, false, "Title Of Note", "Test"
                        )
                    )
                )
            )
        )
    }
}