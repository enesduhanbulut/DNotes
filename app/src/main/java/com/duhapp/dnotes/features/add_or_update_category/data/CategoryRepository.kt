package com.duhapp.dnotes.features.add_or_update_category.data

import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun insert(categoryUIModel: CategoryUIModel)
    suspend fun deleteCategory(categoryUIModel: CategoryUIModel)
    suspend fun updateCategory(categoryUIModel: CategoryUIModel)
    fun getCategories(): Flow<List<CategoryUIModel>>
}