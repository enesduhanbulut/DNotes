package com.duhapp.dnotes.features.select_category.domain

import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel
import kotlinx.coroutines.flow.Flow

class GetCategories(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<List<CategoryUIModel>> {
        return categoryRepository.getCategories()
    }
}