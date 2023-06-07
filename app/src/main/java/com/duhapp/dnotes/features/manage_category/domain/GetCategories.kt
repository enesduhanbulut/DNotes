package com.duhapp.dnotes.features.manage_category.domain

import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
class GetCategories(
    private val categoryRepository: CategoryRepository
) {
    suspend fun invoke(): List<CategoryUIModel> {
        return categoryRepository.getCategories()
    }
}