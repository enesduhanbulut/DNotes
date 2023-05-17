package com.duhapp.dnotes.features.add_or_update_category.domain

import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel

class DeleteCategory(private val categoryRepository: CategoryRepository) {
    suspend operator fun invoke(categoryUIModel: CategoryUIModel) {
        categoryRepository.deleteCategory(categoryUIModel)
    }
}