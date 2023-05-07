package com.duhapp.dnotes.ui.home

import com.duhapp.dnotes.base.ui.BaseUIState
import com.duhapp.dnotes.home_category.ui.HomeCategoryUIModel

data class HomeUIState(
    val categories: List<HomeCategoryUIModel>
) : BaseUIState()