package com.duhapp.dnotes.features.add_or_update_category.data

import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel

interface CategoryRepository {
    suspend fun insert(categoryUIModel: CategoryUIModel)
    suspend fun deleteCategory(categoryUIModel: CategoryUIModel)
    suspend fun updateCategory(categoryUIModel: CategoryUIModel)
}