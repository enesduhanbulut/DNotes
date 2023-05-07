package com.duhapp.dnotes.home_category.ui

data class HomeCategoryUIModel(
    val id: Int,
    val title: String,
    val noteList: List<BaseNoteUIModel>
)