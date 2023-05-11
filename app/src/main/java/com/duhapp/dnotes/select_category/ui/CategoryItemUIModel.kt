package com.duhapp.dnotes.select_category.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class CategoryItemUIModel(
    @DrawableRes val icon: Int,
    val title: String,
    val id: Int,
    val description: String,
    @ColorRes val color: Int
)