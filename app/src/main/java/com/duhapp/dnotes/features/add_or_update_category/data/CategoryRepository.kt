package com.duhapp.dnotes.features.add_or_update_category.data

import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import com.duhapp.dnotes.features.home.home_screen_category.ui.BaseNoteUIModel

interface CategoryRepository {
    suspend fun insert(categoryUIModel: CategoryUIModel): Int
    suspend fun deleteCategory(categoryUIModel: CategoryUIModel, categoryNotes: List<BaseNoteUIModel>)
    suspend fun updateCategory(categoryUIModel: CategoryUIModel)
    suspend fun getCategories(): List<CategoryUIModel>
    suspend fun getById(id: Int): CategoryUIModel?
    suspend fun undo(): Boolean
}