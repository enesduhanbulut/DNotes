package com.duhapp.dnotes.features.manage_category.domain

import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel

class CreateDefaultCategory(
    private val categoryRepository: CategoryRepository,
    private val defaultCategoryModel: CategoryUIModel
) {
    suspend fun invoke() {
        val category = categoryRepository.getById(defaultCategoryModel.id)
        if (category == null) {
            categoryRepository.insert(defaultCategoryModel)
        }
    }
}