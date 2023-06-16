package com.duhapp.dnotes.features.note.domain

import com.duhapp.dnotes.features.add_or_update_category.data.CategoryRepository
import com.duhapp.dnotes.features.add_or_update_category.ui.CategoryUIModel

class GetDefaultCategory(
    private val categoryRepository: CategoryRepository,
    private val categoryUIModel: CategoryUIModel
) {
    suspend fun invoke() : CategoryUIModel {
        return categoryRepository.getById(categoryUIModel.id) ?: categoryUIModel
    }
}