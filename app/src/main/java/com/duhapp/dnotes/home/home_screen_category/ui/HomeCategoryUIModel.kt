package com.duhapp.dnotes.home.home_screen_category.ui

data class HomeCategoryUIModel(
    val id: Int,
    val title: String,
    val noteList: List<BaseNoteUIModel>
)