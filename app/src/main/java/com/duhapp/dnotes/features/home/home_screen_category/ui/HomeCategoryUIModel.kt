package com.duhapp.dnotes.features.home.home_screen_category.ui

import com.duhapp.dnotes.features.base.ui.BaseListItem

data class HomeCategoryUIModel(
    val id: Int,
    val title: String,
    val noteList: List<BaseNoteUIModel>
) : BaseListItem